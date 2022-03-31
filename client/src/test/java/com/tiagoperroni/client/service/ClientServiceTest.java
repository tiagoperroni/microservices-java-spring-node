    package com.tiagoperroni.client.service;

    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.tiagoperroni.client.config.ClientServiceConfig;
    import com.tiagoperroni.client.config.IFeignAdress;
    import com.tiagoperroni.client.exceptions.ClientNotFoundException;
    import com.tiagoperroni.client.exceptions.DataNotFoundException;
    import com.tiagoperroni.client.exceptions.DuplicatedClientException;
    import com.tiagoperroni.client.exceptions.InvalidTokenException;
    import com.tiagoperroni.client.feign.TokenFeignRequest;

    import com.tiagoperroni.client.model.*;
    import com.tiagoperroni.client.repository.ClientRepository;

    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import static org.mockito.Mockito.*;

    import org.mockito.MockitoAnnotations;
    import org.springframework.core.env.Environment;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;

    import java.util.HashMap;
    import java.util.Map;
    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.*;

    public class ClientServiceTest {

        @InjectMocks
        private ClientService clientService;

        @Mock
        private ClientServiceConfig clientServiceConfig;

        @Mock
        private ClientRepository clientRepository;

        @Mock
        private IFeignAdress feignAdress;

        @Mock
        private TokenFeignRequest tokenFeignRequest;

        @Mock
        private Environment environment;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void testGetUserWithEmail_Sucess() {

            when(this.clientRepository.findByEmail(anyString())).thenReturn(this.returnClientOptional());

            ClientResponse response = this.clientService.getClient("tiagoperroni@gmail.com");

            assertEquals("Tiago Perroni", response.getName());

        }

        @Test
        public void testGetUserWithEmail_ReturnNull() {

            Optional<Client> client = Optional.of(new Client());
            when(this.clientRepository.findByEmail(anyString())).thenReturn(client);

            ClientResponse response = this.clientService.getClient("tiagoperroni@gmail.com");

            assertNull(response.getName());
        }

        @Test
        public void testSaveNewClient_Success() {

            Optional<Client> client = Optional.of(new Client());
            client.get().setCpf("");
            when(this.clientRepository.findByCpf(anyString())).thenReturn(client);

            Optional<Client> client2 = Optional.of(new Client());
            client2.get().setEmail("");
            when(this.clientRepository.findByEmail(anyString())).thenReturn(client2);

            when(this.feignAdress.getAdress(anyString()))
                    .thenReturn(new ResponseEntity<AdressRequest>(this.getAdressRequest(), HttpStatus.OK));

            when(this.environment.getProperty("local.server.port")).thenReturn("8081");

            when(this.clientRepository.save(any())).thenReturn(this.returnClient());

            ClientResponse response = this.clientService.saveClient(this.getClientRequest());

            assertEquals("Tiago Perroni", response.getName());
            assertEquals("******", response.getPassword());
        }

        @Test
        public void testUpdateClient_Success() {

            when(this.tokenFeignRequest.getToken(anyString())).thenReturn(new ResponseEntity<String>("123456789", HttpStatus.OK));

            when(this.clientRepository.findByEmail(anyString())).thenReturn(this.returnClientOptional());

            when(this.clientRepository.save(any())).thenReturn(this.returnClient());

            String response = this.clientService.updateClient("tiagoperroni@gmail.com", this.getClientRequest(), "123456789");

            assertTrue(response.startsWith("Client was update with success"));
        }

        @Test
        public void testUpdateClientWithNullEmail_ShouldFail() {

            when(this.tokenFeignRequest.getToken(anyString())).thenReturn(new ResponseEntity<String>("123456789", HttpStatus.OK));
            try {
                var clientRequest = new ClientRequest();
                clientRequest.setEmail("");
                this.clientService.updateClient("tiagoperroni@gmail.com", clientRequest, "123456789");
            }catch (Exception ex) {
                assertEquals(DataNotFoundException.class, ex.getClass());
                assertEquals("Do you need informe the CPF and E-mail.", ex.getMessage());
            }
        }

        @Test
        public void testUpdateClientWithDuplicatedEmail_ShouldFail() {

            when(this.tokenFeignRequest.getToken(anyString())).thenReturn(new ResponseEntity<String>("123456789", HttpStatus.OK));

            when(this.clientRepository.findByEmail(anyString())).thenReturn(this.returnClientOptional());
            try {
                var clientRequest = new ClientRequest();
                clientRequest.setEmail("tiagoperroni@gmail.com");
                clientRequest.setCpf("12365498");
                this.clientService.updateClient("tiagoperroni@gmail.com", clientRequest, "123456789");
            }catch (Exception ex) {
                assertEquals(DuplicatedClientException.class, ex.getClass());
                assertEquals("Already exists a client with the CPF informed.", ex.getMessage());
            }
        }

        @Test
        public void testUpdateClientWithNullClient_ShouldFail() {

            when(this.tokenFeignRequest.getToken(anyString())).thenReturn(new ResponseEntity<String>("123456789", HttpStatus.OK));

            //when(this.clientRepository.findByEmail(anyString())).thenReturn(this.returnClientOptional());
            try {
                var clientRequest = new ClientRequest();
                clientRequest.setEmail("tiagoperroni@gmail.com");
                clientRequest.setCpf("12365498");
                this.clientService.updateClient("tiagoperroni@gmail.com", clientRequest, "123456789");
            }catch (Exception ex) {
                assertEquals(ClientNotFoundException.class, ex.getClass());
                assertEquals("Not found a client with email tiagoperroni@gmail.com", ex.getMessage());
            }
        }

        @Test
        public void testVerifyClientCpfAndEmailAlreadyExists_ShouldFail() {

            when(this.clientRepository.findByEmail(anyString())).thenReturn(this.returnClientOptional());
            try {
                var clientRequest = new ClientRequest();
                clientRequest.setEmail("tiagoperroni@gmail.com");
                clientRequest.setCpf("12365498");
                this.clientService.verifyClientCpfAndEmailAlreadyExists("123456789", "tiagoperroni@gmail.com");
            }catch (Exception ex) {
                assertEquals(DuplicatedClientException.class, ex.getClass());
                assertEquals("Already exists a client with the e-mail informed.", ex.getMessage());
            }
        }

        @Test
        public void testVerifyClientCpfAndCPFlAlreadyExists_ShouldFail() {

            Optional<Client> client = Optional.of(new Client());
            client.get().setEmail("");
            when(this.clientRepository.findByEmail(anyString())).thenReturn(client);
            when(this.clientRepository.findByCpf(anyString())).thenReturn(this.returnClientOptional());
            try {
                var clientRequest = new ClientRequest();
                clientRequest.setEmail("tiagoperroni@gmail.com");
                clientRequest.setCpf("12365498");
                this.clientService.verifyClientCpfAndEmailAlreadyExists("123456789", "tiagoperroni@gmail.com");
            }catch (Exception ex) {
                assertEquals(DuplicatedClientException.class, ex.getClass());
                assertEquals("Already exists a client with the CPF informed.", ex.getMessage());
            }
        }

        @Test
        public void testVerifyToken_ShouldFail() {

            when(this.tokenFeignRequest.getToken(anyString())).thenReturn(new ResponseEntity<String>("123456789", HttpStatus.OK));
            try {
                this.clientService.verifyToken("tiagoperroni@gmail.com", "123456767");
            }catch (Exception ex) {
                assertEquals(InvalidTokenException.class, ex.getClass());
                assertEquals("The token informed is invalid.", ex.getMessage());
            }
        }

        @Test
        public void testDeleteClient_Success() {

            when(this.tokenFeignRequest.getToken(anyString())).thenReturn(new ResponseEntity<String>("123456789", HttpStatus.OK));

            when(this.clientRepository.findByEmail(anyString())).thenReturn(this.returnClientOptional());

            Map<String, String> response = this.clientService.deleteClient("tiagoperroni@gmail.com", "123456789");

            var message = new HashMap<String, String>();
            message.put("Message", "User was deleted with success.");
            assertEquals(message, response);
        }

        @Test
        public void testGetPropertiesDetails() throws JsonProcessingException {

            var response =  this.clientService.getPropertiesDetails();
            assertNotNull(response);
            assertTrue(response.toString().startsWith("{buildVersion"));
        }

        @Test
        public void testGetClientLogin() throws JsonProcessingException {

            when(this.clientRepository.findByEmail(anyString())).thenReturn(this.returnClientOptional());

            var response =  this.clientService.getClientLogin("tiagoperroni@gmail.com");
            assertNotNull(response);
            assertEquals("tiagoperroni@gmail.com", response.getEmail());
            assertEquals("052.586.987-56", response.getCpf());
        }

        @Test
        public void testClientLogin() throws JsonProcessingException {

            when(this.tokenFeignRequest.getTokenLogin(any())).thenReturn(new ResponseEntity<String>("1236547", HttpStatus.OK));

            var clientLogin = new ClientLogin();
            clientLogin.setEmail("tiago@gmail.com");
            clientLogin.setPassword("12345");
            var response =  this.clientService.clientLogin(clientLogin);

            assertNotNull(response);
            assertEquals("1236547", response);
        }

        /**
         * método para instanciar um cliente
         *
         * @return
         */
        public Client returnClient() {
           Client client = new Client();
            client.setId(1);
            client.setName("Tiago Perroni");
            client.setEmail("tiagoperroni@gmail.com");
            client.setCpf("052.586.987-56");
            client.setPassword("1234");
            client.setIsActive(true);
            client.setClientPort("8081");
            return client;
        }
        public Optional<Client> returnClientOptional() {
            Optional<Client> client = Optional.of(new Client());
            client.get().setId(1);
            client.get().setName("Tiago Perroni");
            client.get().setEmail("tiagoperroni@gmail.com");
            client.get().setCpf("052.586.987-56");
            client.get().setPassword("1234");
            client.get().setIsActive(true);
            client.get().setClientPort("8081");
            return client;
        }

        public ClientRequest getClientRequest() {
            ClientRequest clientRequest = new ClientRequest();
            clientRequest.setName("Tiago Perroni");
            clientRequest.setCpf("052.586.987-56");
            clientRequest.setEmail("tiagoperroni@gmail.com");
            clientRequest.setCep("87710130");
            clientRequest.setComplement("Casa");
            clientRequest.setNumber("290");
            clientRequest.setPassword("1234");
            clientRequest.setRepeatPassword("1234");
            return clientRequest;
        }

        public AdressRequest getAdressRequest() {
            AdressRequest adressRequest = new AdressRequest();
            adressRequest.setLocalidade("Curitiba");
            adressRequest.setUf("PR");
            adressRequest.setCep("87710130");
            adressRequest.setLogradouro("Rua a");
            adressRequest.setBairro("Bairro a");
            return adressRequest;
        }

    }
