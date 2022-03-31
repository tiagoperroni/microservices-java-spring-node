package com.tiagoperroni.client.model;

import com.tiagoperroni.client.config.ClientServiceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ClientServiceConfigTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testClientServiceConfigModel() {
        var clientServiceConfig = new ClientServiceConfig();
        clientServiceConfig.setMsg("Alguma coisa");
        clientServiceConfig.setProperties("Properties");

        Assertions.assertEquals("Alguma coisa", clientServiceConfig.getMsg());
    }
}
