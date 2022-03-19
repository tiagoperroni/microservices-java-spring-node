package com.tiagoperroni.client.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.config.ClientServiceConfig;
import com.tiagoperroni.client.config.IFeignAdress;
import com.tiagoperroni.client.mapper.AdressMapper;
import com.tiagoperroni.client.mapper.ClientMapper;
import com.tiagoperroni.client.model.AdressRequest;
import com.tiagoperroni.client.model.AdressResponse;
import com.tiagoperroni.client.model.ClientRequest;
import com.tiagoperroni.client.model.ClientResponse;
import com.tiagoperroni.client.repository.ClientRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public ClientResponse getClient(Integer id) {
        logger.info("Service: Received a client request with id: {}", id);
        var client = this.clientRepository.findById(id).orElse(null);       
        return client;
    }  
    
    public ClientResponse clientRequest(ClientRequest request) {
        logger.info("Service: Prepare client response with request: {}", request);
        var adressResponse = this.getAdress(request.getCep(), request.getNumber(), request.getComplement());
        logger.info("Service: Prepare client response with Client Mapper");
        var clientResponse = ClientMapper.convert(request, adressResponse); 
        clientResponse.setClientPort(this.environment.getProperty("server.port"));
        logger.info("Service: Sending client response with response: {}", clientResponse);        
        return this.clientRepository.save(clientResponse);    
    }

    public AdressResponse getAdress(String cep, String number, String complement) {
        logger.info("Service: Prepare Adress Response with cep: {}, number: {}, complement: {} ", cep, number, complement);  
        AdressRequest request = this.feignAdress.getAdress(cep).getBody();        
        var adressResponse = AdressMapper.convert(request, number, complement);
        logger.info("Service: Sending client Adress Response with response: {}", adressResponse);
        return adressResponse;
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
