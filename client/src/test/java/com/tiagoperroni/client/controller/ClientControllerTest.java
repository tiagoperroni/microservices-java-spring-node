package com.tiagoperroni.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.exceptions.ClientNotFoundException;
import com.tiagoperroni.client.model.Client;
import com.tiagoperroni.client.service.ClientService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getClientById_Sucess() {

        when(this.clientService.getClient(anyInt())).thenReturn(new Client(1, "Tiago Perroni", "054.564.987-23", true));

        ResponseEntity<Client> client = this.clientController.getClient(1);

        assertEquals("Tiago Perroni", client.getBody().getName());
        assertTrue(client.getBody().getIsActive());
    }

    @Test
    public void getClientById_Fail() {

        when(this.clientService.getClient(anyInt())).thenThrow(ClientNotFoundException.class);

        Exception exception = assertThrows(ClientNotFoundException.class, () ->
                this.clientController.getClient(12));
        assertEquals(exception.getClass(), ClientNotFoundException.class);
    }

    @Test
    public void getProperties_Sucess() throws JsonProcessingException {

        var prop = new HashMap<String, Object>();
        prop.put("mailDetails", "details");
        when(this.clientService.getPropertiesDetails()).thenReturn(prop);

        ResponseEntity<Map<String, Object>> properties = this.clientController.getClientProperties();
        assertTrue(properties.getBody().containsKey("mailDetails"));
    }
}
