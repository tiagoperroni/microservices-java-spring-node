package com.tiagoperroni.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.config.ClientServiceConfig;
import com.tiagoperroni.client.config.IFeignAdress;
import com.tiagoperroni.client.mapper.AdressMapper;
import com.tiagoperroni.client.model.AdressRequest;
import com.tiagoperroni.client.model.AdressResponse;
import com.tiagoperroni.client.model.ClientResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private Environment environment;

    @Autowired
    private IFeignAdress feignAdress;

    private Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientServiceConfig clientServiceConfig;

    public ClientResponse getClient(Integer id) {
        logger.info("Microservice port: {}", environment.getProperty("local.server.port"));

        for (ClientResponse client : this.getClients()) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }
    
    public List<ClientResponse> getClients() {
        var adressResponse = this.getAdress("87710140", "290", "casa");
        var clientResponse = new ClientResponse();
        clientResponse.setId(1);
        clientResponse.setName("Tiago Perroni");
        clientResponse.setCpf("052.456.987-23");
        clientResponse.setIsActive(true);
        clientResponse.setClientPort(this.environment.getProperty("server.port"));
        clientResponse.setAdress(adressResponse);
        return new ArrayList<>(Arrays.asList(clientResponse));
              
    }

    public AdressResponse getAdress(String cep, String numero, String complemento) {
        logger.info("Enviando requisição de endereço cep: {}", cep);
        AdressRequest request = this.feignAdress.getAdress(cep).getBody();
        logger.info("Exibindo endereço recebido: {}", request);
        var adressResponse = AdressMapper.convert(request, numero, complemento);
        logger.info("Enviando endereço completo: {}", adressResponse);
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
