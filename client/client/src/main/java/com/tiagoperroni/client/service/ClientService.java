package com.tiagoperroni.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tiagoperroni.client.model.Client;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    public Client getClient(Integer id) {
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
                new Client(1, "Tiago Perroni", "045.986.789-89", true),
                new Client(2, "Maria Silva", "074.165.174-23", true),
                new Client(3, "José Soares", "098.652.258-11", false)));
    }
}
