package com.tiagoperroni.client.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.config.ClientServiceConfig;
import com.tiagoperroni.client.config.IFeignAdress;
import com.tiagoperroni.client.exceptions.ClientNotFoundException;
import com.tiagoperroni.client.exceptions.DuplicatedClientException;
import com.tiagoperroni.client.mapper.AdressMapper;
import com.tiagoperroni.client.mapper.ClientMapper;
import com.tiagoperroni.client.model.AdressRequest;
import com.tiagoperroni.client.model.AdressResponse;
import com.tiagoperroni.client.model.ClientLogin;
import com.tiagoperroni.client.model.ClientRequest;
import com.tiagoperroni.client.model.ClientResponse;
import com.tiagoperroni.client.repository.ClientRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private IFeignAdress feignAdress;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private Environment environment;

    private Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientServiceConfig clientServiceConfig;

    public ClientResponse getClient(String email) {
        logger.info("Service: Received a client request with id: {}", email);
        var client = this.clientRepository.findByEmail(email).orElse(null);
        return ClientMapper.convertFromClient(client);
    }

    public ClientResponse saveClient(ClientRequest request) {
        logger.info("Service: Prepare client response with request: {}", request);
        this.verifyClientCpfAndEmailAlreadyExists(request.getCpf(), request.getEmail());
        var adressResponse = this.getAdress(request.getCep(), request.getNumber(), request.getComplement());
        logger.info("Service: Prepare client response with Client Mapper");
        var client = ClientMapper.convertFromClientRequest(request, adressResponse);
        client.setClientPort(this.environment.getProperty("server.port"));
        logger.info("Service: Sending client response with response: {}", client);
        return ClientMapper.convertFromClient(this.clientRepository.save(client));
    }

    public String updateClient(String email, ClientRequest request) {
        var client = this.verifyIfClientWasFound(email);
        if (client.getCpf().equals(request.getCpf())) {
            BeanUtils.copyProperties(request, client);
            this.clientRepository.save(ClientMapper.convertFromClientResponse(client));
            return "Client was update with success. " + client;
        }
        throw new DuplicatedClientException("Already exists a client with the CPF informed.");
    }

    public AdressResponse getAdress(String cep, String number, String complement) {
        logger.info("Service: Prepare Adress Response with cep: {}, number: {}, complement: {} ", cep, number,
                complement);
        AdressRequest request = this.feignAdress.getAdress(cep).getBody();
        var adressResponse = AdressMapper.convert(request, number, complement);
        logger.info("Service: Sending client Adress Response with response: {}", adressResponse);
        return adressResponse;
    }

    public ClientResponse verifyIfClientWasFound(String email) {
        var client = this.getClient(email);
        if (client == null) {
            throw new ClientNotFoundException(String.format("Not found a client with id %s ", email));
        }
        return client;
    }

    public void verifyClientCpfAndEmailAlreadyExists(String cpf, String email) {
        var clientCpf = this.clientRepository.findByCpf(cpf).orElse(null);
        var clientEmail = this.clientRepository.findByCpf(cpf).orElse(null);
        if (clientCpf != null) {
            throw new DuplicatedClientException("Already exists a client with the CPF informed.");
        } else {
            if (clientEmail != null) {
                throw new DuplicatedClientException("Already exists a client with the e-mail informed.");
            }
        }

    }

    public ClientResponse findByEmail(ClientLogin clientLogin) {
        var client = this.clientRepository.findByEmail(clientLogin.getEmail())
                .orElseThrow(() -> new ClientNotFoundException(
                        String.format("Not found client with the e-mail %s.", clientLogin.getEmail())));
        return ClientMapper.convertFromClient(client);

    }

    public Map<String, Object> getPropertiesDetails() throws JsonProcessingException {

        Properties properties = new Properties();
        properties.put("mailDetails", this.clientServiceConfig.getMailDetails());

        var propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("message", this.clientServiceConfig.getMsg());
        propertiesMap.put("buildVersion", this.clientServiceConfig.getBuildVersion());
        propertiesMap.put("repo", this.clientServiceConfig.getRepo());
        propertiesMap.put("mailDetails", properties.toString());
        return propertiesMap;
    }
}
