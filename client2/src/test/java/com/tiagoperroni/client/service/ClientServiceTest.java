package com.tiagoperroni.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.config.ClientServiceConfig;
import com.tiagoperroni.client.exceptions.ClientNotFoundException;
import com.tiagoperroni.client.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientServiceConfig clientServiceConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUserWithId_Sucess() {

        Client client = this.clientService.getClient(2);
        assertEquals("Maria Silva", client.getName());

    }

    @Test()
    public void getUserWithId_Fail() {
        Exception exception = assertThrows(ClientNotFoundException.class, () ->
                clientService.getClient(12));
        assertEquals("Cliente de id 12 não encontrado.", exception.getMessage());
        assertEquals(exception.getClass(), ClientNotFoundException.class);
    }

    @Test
    public void getClientsList_Sucess() {

        List<Client> getClients = this.clientService.getClients();

        assertNotNull(getClients);
        assertTrue("Tiago Perroni" == getClients.get(0).getName());
    }

    @Test
    public void getPropertiesDetails() throws JsonProcessingException {

        // ação
        Map<String, Object> properties = this.clientService.getPropertiesDetails();

        // resultado
        assertNotNull(properties);
    }
}
