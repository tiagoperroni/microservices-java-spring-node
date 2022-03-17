package com.tiagoperroni.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.config.ClientServiceConfig;
import com.tiagoperroni.client.exceptions.ClientNotFoundException;
import com.tiagoperroni.client.model.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientServiceConfig clientServiceConfig;

    public Client getClient(Integer id) {
        for (Client client : this.getClients()) {
            if (client.getId() == id) {
                return client;
            }
        }
        throw new ClientNotFoundException(String.format("Cliente de id %s não encontrado.", id));
    }

    @Bean
    public List<Client> getClients() {
        return new ArrayList<>(Arrays.asList(
                new Client(1, "Tiago Perroni", "045.986.789-89", true),
                new Client(2, "Maria Silva", "074.165.174-23", true),
                new Client(3, "José Soares", "098.652.258-11", false)));
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
