package com.tiagoperroni.address.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

import com.tiagoperroni.address.model.Adress;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AdressControllerTest {

    @InjectMocks
    private AdressController adressController;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAdress_Success() {

        var adress = new Adress();
        adress.setCep("87710130");
        adress.setLogradouro("Avenida Euclides da cunha");

        when(this.restTemplate.getForEntity("http://viacep.com.br/ws/87710130/json/", Adress.class))
            .thenReturn(new ResponseEntity<Adress>(adress, HttpStatus.OK));

        ResponseEntity<Adress> response = this.adressController.getAdress("87710130");

        assertEquals("87710130", response.getBody().getCep());
        assertEquals("Avenida Euclides da cunha", response.getBody().getLogradouro());
    }
    
}
