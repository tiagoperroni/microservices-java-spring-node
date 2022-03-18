package com.tiagoperroni.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.config.ClientServiceConfig;
import com.tiagoperroni.client.model.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private Environment environment;

    private Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientServiceConfig clientServiceConfig;

    public Client getClient(Integer id) {
        logger.info("Porta do microserviço: {}", environment.getProperty("local.server.port"));

        for (Client client : this.getClients()) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

    @Bean
    public List<Client> getClients() {
        return new ArrayList<>(Arrays.asList(
                new Client(1, "Tiago Perroni", "045.986.789-89", true, environment.getProperty("local.server.port")),
                new Client(2, "Maria Silva", "074.165.174-23", true, environment.getProperty("local.server.port")),
                new Client(3, "José Soares", "098.652.258-11", false, environment.getProperty("local.server.port"))));
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
