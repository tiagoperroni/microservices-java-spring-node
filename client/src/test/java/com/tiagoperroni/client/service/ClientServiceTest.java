package com.tiagoperroni.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.client.config.ClientServiceConfig;
import com.tiagoperroni.client.config.IFeignAdress;
import com.tiagoperroni.client.exceptions.ClientNotFoundException;
import com.tiagoperroni.client.model.AdressRequest;
import com.tiagoperroni.client.model.Client;
import com.tiagoperroni.client.model.ClientResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientServiceConfig clientServiceConfig;

    @Mock
    private IFeignAdress feignAdress;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUserWithId_Sucess() {

        var adressRequest = new AdressRequest();
        adressRequest.setLocalidade("Paranavaí");
        when(this.feignAdress.getAdress("87710140")).thenReturn(new ResponseEntity<AdressRequest>(adressRequest, HttpStatus.OK));
        
        var clientResponse = new ClientResponse();
        clientResponse.setName("Tiago Perroni");
        when(this.clientService.getClient(1)).thenReturn(clientResponse);

        ClientResponse client = this.clientService.getClient(1);
        assertEquals("Tiago Perroni", client.getName());
        assertEquals("Paranavaí", client.getAdressResponse().getCidade());

    }

    // @Test()
    // public void getUserWithId_Fail() {
    //     Exception exception = assertThrows(ClientNotFoundException.class, () ->
    //             clientService.getClient(12));
    //     assertEquals("Cliente de id 12 não encontrado.", exception.getMessage());
    //     assertEquals(exception.getClass(), ClientNotFoundException.class);
    // }

    // @Test
    // public void getClientsList_Sucess() {

    //     List<Client> getClients = this.clientService.getClients();

    //     assertNotNull(getClients);
    //     assertTrue("Tiago Perroni" == getClients.get(0).getName());
    // }

    @Test
    public void getPropertiesDetails() throws JsonProcessingException {

        // ação
        Map<String, Object> properties = this.clientService.getPropertiesDetails();

        // resultado
        assertNotNull(properties);
    }
}
