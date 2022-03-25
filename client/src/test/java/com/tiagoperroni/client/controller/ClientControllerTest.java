 package com.tiagoperroni.client.controller;

 import com.fasterxml.jackson.core.JsonProcessingException;
 import com.tiagoperroni.client.exceptions.ClientNotFoundException;
 import com.tiagoperroni.client.model.Client;
 import com.tiagoperroni.client.model.ClientRequest;
 import com.tiagoperroni.client.model.ClientResponse;
 import com.tiagoperroni.client.service.ClientService;
 import static org.junit.jupiter.api.Assertions.*;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import static org.mockito.Mockito.*;
 import org.mockito.MockitoAnnotations;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;

 import java.util.HashMap;
 import java.util.Map;

 public class ClientControllerTest {

     @InjectMocks
     private ClientController clientController;

     @Mock
     private ClientService clientService;

     @Mock
     private Throwable throwable;

     @BeforeEach
     public void setUp() {
         MockitoAnnotations.initMocks(this);
     }

     @Test
     public void getClientByEmail_Sucess() {

         when(this.clientService.getClient(anyString())).thenReturn(this.getClientResponse());

         ResponseEntity<ClientResponse> client = this.clientController.getClient("tiagoperroni@gmail.com");

         assertEquals("Tiago Perroni", client.getBody().getName());
         assertTrue(client.getStatusCode().toString().equals("200 OK"));
     }

     @Test
     public void testRequestClient_Sucess() {

         when(this.clientService.saveClient(any())).thenReturn(this.getClientResponse());

         ResponseEntity<ClientResponse> client = this.clientController.requestClient(new ClientRequest());

         assertEquals("Tiago Perroni", client.getBody().getName());
         assertTrue(client.getStatusCode().toString().equals("201 CREATED"));
     }

     @Test
     public void testUpdateClient_Sucess() {

         when(this.clientService.updateClient(anyString(), any(), anyString())).thenReturn("Client was update with success.");

         ResponseEntity<String> client = this.clientController.updateClient("tiago" ,new ClientRequest(), "123456789");

         assertTrue(client.getStatusCode().toString().startsWith("202 ACCEPTED"));
         assertTrue(client.toString().startsWith("<202 ACCEPTED Accepted,Client was update"));
     }

     @Test
     public void testDeleteClient_Sucess() {

         var response = new HashMap<String, String>();
         response.put("Message", "User was deleted with success.");

         when(this.clientService.deleteClient(anyString(), anyString())).thenReturn(response);

         ResponseEntity<Map<String, String>> client = this.clientController.deleteClient("tiago",  "123456789");

         assertTrue(client.getStatusCode().toString().startsWith("202 ACCEPTED"));
         assertTrue(client.toString().startsWith("<202 ACCEPTED Accepted,{Message=User was delete"));
     }

     @Test
     public void testRequestAdressClientFallback_Sucess() {

         var response = new HashMap<String, String>();
         response.put("Message", "User was deleted with success.");
         var ex = new Throwable("Already exists a client with the CPF");

         ResponseEntity<Map<String, String>> client = this.clientController.requestAdressClientFallback(ex);

         assertEquals("400 BAD_REQUEST", client.getStatusCode().toString());
         assertTrue(client.toString().startsWith("<400 BAD_REQUEST Bad Request,{Error=Already exists"));

     }

     @Test
     public void testRequestAdressClientFallbackFirstCondition_Sucess() {

         var ex = new Throwable("Already exists a client with the CPF informed.");

         ResponseEntity<Map<String, String>> client = this.clientController.requestAdressClientFallback(ex);

         assertEquals("400 BAD_REQUEST", client.getStatusCode().toString());
         System.out.println(client);
         assertTrue(client.toString().startsWith("<400 BAD_REQUEST Bad Request,{Error=Already exists"));

     }


     @Test
     public void getProperties_Sucess() throws JsonProcessingException {

         var prop = new HashMap<String, Object>();
         prop.put("mailDetails", "details");
         when(this.clientService.getPropertiesDetails()).thenReturn(prop);

         ResponseEntity<Map<String, Object>> properties = this.clientController.getClientProperties();
         assertTrue(properties.getBody().containsKey("mailDetails"));
     }

     public ClientResponse getClientResponse() {
         ClientResponse client = new ClientResponse();
         client.setEmail("tiagoperroni@gmail.com");
         client.setName("Tiago Perroni");
         return client;
     }
 }
