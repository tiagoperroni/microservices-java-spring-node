package com.tiagoperroni.order.service;

import com.tiagoperroni.order.exceptions.InvalidTokenException;
import com.tiagoperroni.order.feign.AuthenticationRequestToken;
import com.tiagoperroni.order.models.OrderRequest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ClientLoginServiceTest {

    @InjectMocks
    private ClientLoginService clientLoginService;

    @Mock
    private AuthenticationRequestToken authenticationRequestToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVerifyTokenIsValid() {

        when(this.authenticationRequestToken.getToken(anyString())).thenReturn(new ResponseEntity<String>("12345", HttpStatus.OK));

        var orderRequest = new OrderRequest();
        orderRequest.setClientEmail("tiagoperroni@gmail.com");

        Boolean response = this.clientLoginService.verifyTokenIsValid(orderRequest, "12345");

        assertTrue(response);
    }

    @Test
    public void testVerifyTokenIsValid_ShouldBeFail() {

        when(this.authenticationRequestToken.getToken(anyString())).thenReturn(new ResponseEntity<String>("12345", HttpStatus.OK));

        var orderRequest = new OrderRequest();
        orderRequest.setClientEmail("tiagoperroni@gmail.com");

        Exception ex = assertThrows(InvalidTokenException.class, () -> {
            Boolean response = this.clientLoginService.verifyTokenIsValid(orderRequest, "12365");
        });

        assertEquals(InvalidTokenException.class, ex.getClass());
        assertEquals("Token is invalid. Try login to get a new Token.", ex.getMessage());
    }
}
