package com.tiagoperroni.order.service;
import com.tiagoperroni.order.feign.AuthenticationRequestToken;
import com.tiagoperroni.order.feign.ClientFeignRequest;
import com.tiagoperroni.order.feign.ProductFeignRequest;
import static org.junit.jupiter.api.Assertions.*;

import com.tiagoperroni.order.kafka.KafkaProducerConfig;
import com.tiagoperroni.order.models.*;
import com.tiagoperroni.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class OrderServiceTest {

    public static final String URL_SERVER_PRODUCT = "http://localhost:3000/product/1/2";
    @InjectMocks
    private OrderService orderService;

    @Mock
    private ClientFeignRequest clientFeignRequest;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientLoginService clientLoginService;

    @Mock
    private ProductFeignRequest productFeignRequest;

    @Mock
    private OrderMessageService orderMessageService;

    @Mock
    private KafkaProducerConfig kafka;

    @Mock
    private AuthenticationRequestToken authenticationRequestToken;

    //@Mock
    //private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMakeOrder_Success() {

        var clientRequest = new ClientRequest();
        clientRequest.setId(1);
        clientRequest.setEmail("tiago@gmail.com");
        clientRequest.setIsActive(true);
        var adress = new AdressRequest();
        adress.setCep("8754");
        clientRequest.setAdress(adress);

        when(this.authenticationRequestToken.getToken(anyString()))
                .thenReturn(new ResponseEntity<String>("12345", HttpStatus.OK));

        when(this.clientFeignRequest.getClient(anyString()))
                .thenReturn(new ResponseEntity<ClientRequest>(clientRequest, HttpStatus.OK));

        var orderRequest = new OrderRequest();
        orderRequest.setClientEmail("tiago@gmail.com");
        when(this.clientLoginService.verifyTokenIsValid(orderRequest, "12345")).thenReturn(true);

        var product = new Product();
        product.setId("123");
        product.setPrice(7.98);
        product.setStock(100);
        when(this.productFeignRequest.getProductById(anyString()))
                .thenReturn(new ResponseEntity<Product>(product, HttpStatus.OK));

        var order = new Order();
        order.setId(1); order.setClientEmail("tiago@gmail.com");
        when(this.orderRepository.save(any())).thenReturn(order);

        when(this.productFeignRequest.updateStockRequest(anyString(), anyInt())).thenReturn(new ResponseEntity<Void>(HttpStatus.OK));

        doNothing().when(this.orderMessageService).sendOrderMessage(any());

        doNothing().when(this.kafka).sendMessage(anyString());

        var newOrderRequest = new OrderRequest();
        newOrderRequest.setClientEmail("tiago@gmail.com");
        var newProduct = new ProductList();
        newProduct.setId("123"); newProduct.setQuantity(3);
        List<ProductList> productList = new ArrayList<>(Arrays.asList(newProduct));
        newOrderRequest.setProductList(productList);
        var newOrder = this.orderService.makeOrder(newOrderRequest, "12345");

        assertEquals(1, newOrder.getId());
        assertEquals("tiago@gmail.com", newOrder.getClient().getEmail());
    }

}
