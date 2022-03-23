package com.tiagoperroni.client.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.config.ClientServiceConfig;
import com.tiagoperroni.client.config.IFeignAdress;
import com.tiagoperroni.client.exceptions.ClientNotFoundException;
import com.tiagoperroni.client.exceptions.DataNotFoundException;
import com.tiagoperroni.client.exceptions.DuplicatedClientException;
import com.tiagoperroni.client.exceptions.InvalidTokenException;
import com.tiagoperroni.client.feign.TokenFeignRequest;
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
    private TokenFeignRequest tokenFeignRequest;

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
        client.setClientPort(this.environment.getProperty("local.server.port"));
        logger.info("Service: Sending client response with response: {}", client);
        var clientResponse = ClientMapper.convertFromClient(this.clientRepository.save(client));
        clientResponse.setPassword("******");
        return clientResponse;
    }

    /**
     * método para atualizar cliente
     * 
     * @param email
     * @param request
     * @return
     */

    public String updateClient(String email, ClientRequest request, String token) {
        this.verifyToken(email, token);
        if (email == null || request.getCpf() == null) {
            throw new DataNotFoundException("Do you need informe the CPF and E-mail.");
        }
        var findClient = ClientMapper.convertFromClientResponse(this.verifyIfClientWasFound(email));
        if (findClient.getCpf().equals(request.getCpf())) {
            BeanUtils.copyProperties(request, findClient, "id", "isActive", "password", "clientPort");
            var client = this.clientRepository.save(findClient);
            client.setPassword("******");
            return "Client was update with success. " + ClientMapper.convertFromClient(client);
        }
        throw new DuplicatedClientException("Already exists a client with the CPF informed.");
    }

    /**
     * método para deletar client
     * 
     * @param cep
     * @param number
     * @param complement
     * @return
     */

    public Map<String, String> deleteClient(String email, String token) {
        this.verifyToken(email, token);
        var findClient = ClientMapper.convertFromClientResponse(this.verifyIfClientWasFound(email));
        this.clientRepository.deleteById(findClient.getId());
        var response = new HashMap<String, String>();
        response.put("Message", "User was deleted with success.");
        return response;
    }

    public AdressResponse getAdress(String cep, String number, String complement) {
        logger.info("Service: Prepare Adress Response with cep: {}, number: {}, complement: {} ", cep, number,
                complement);
        AdressRequest request = this.feignAdress.getAdress(cep).getBody();
        var adressResponse = AdressMapper.convert(request, number, complement);
        logger.info("Service: Sending client Adress Response with response: {}", adressResponse);
        return adressResponse;
    }

    /**
     * Verifica se existe client através do email
     * 
     * @param email
     * @return
     */
    public ClientResponse verifyIfClientWasFound(String email) {
        var client = this.clientRepository.findByEmail(email).orElse(null);
        if (client == null) {
            throw new ClientNotFoundException(String.format("Not found a client with id %s ", email));
        }
        return ClientMapper.convertFromClient(client);
    }

    public void verifyClientCpfAndEmailAlreadyExists(String cpf, String email) {
        var clientCpf = this.clientRepository.findByCpf(cpf).orElse(null);

        if (clientCpf != null) {
            throw new DuplicatedClientException("Already exists a client with the CPF informed.");
        }

        var client = this.clientRepository.findByEmail(email).orElse(null);

        if (client != null) {
            throw new DuplicatedClientException("Already exists a client with the e-mail informed.");
        }
    }

    public ClientResponse findByEmail(ClientLogin clientLogin) {
        var client = this.clientRepository.findByEmail(clientLogin.getEmail())
                .orElseThrow(() -> new ClientNotFoundException(
                        String.format("Not found client with the e-mail %s.", clientLogin.getEmail())));
        return ClientMapper.convertFromClient(client);

    }

    public void verifyToken(String email, String token) {
        String tokenRequest = this.tokenFeignRequest.getToken(email).getBody();
        if (!tokenRequest.equals(token)) {
            throw new InvalidTokenException("The token informed is invalid.");
        }
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
