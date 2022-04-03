package com.tiagoperroni.order.service;
import com.tiagoperroni.order.exceptions.*;
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

        when(this.authenticationRequestToken.getToken(anyString()))
                .thenReturn(new ResponseEntity<String>("12345", HttpStatus.OK));

        when(this.clientFeignRequest.getClient(anyString()))
                .thenReturn(new ResponseEntity<ClientRequest>(this.getClientRequest(), HttpStatus.OK));

        when(this.clientLoginService.verifyTokenIsValid(this.getOrderRequest(), "12345")).thenReturn(true);

        when(this.productFeignRequest.getProductById(anyString()))
                .thenReturn(new ResponseEntity<Product>(this.getProduct(), HttpStatus.OK));

        when(this.orderRepository.save(any())).thenReturn(this.getOrder());

        when(this.productFeignRequest.updateStockRequest(anyString(), anyInt())).thenReturn(new ResponseEntity<Void>(HttpStatus.OK));

        doNothing().when(this.orderMessageService).sendOrderMessage(any());

        doNothing().when(this.kafka).sendMessage(anyString());

        var newOrder = this.orderService.makeOrder(this.getRequest(), "12345");

        assertEquals(1, newOrder.getId());
        assertEquals("tiago@gmail.com", newOrder.getClient().getEmail());
    }

    @Test
    public void testGetClientRequest_ShouldBeFail() {

        when(this.clientFeignRequest.getClient(anyString())).thenReturn(new ResponseEntity<ClientRequest>(HttpStatus.OK));

        Exception ex = assertThrows(ClientNotFoundException.class, () -> {
            this.orderService.getClientRequest("tiago@gmail.com");
        });
        assertEquals("Client with e-mail tiago@gmail.com not was found.", ex.getMessage().toString());
        assertEquals(ClientNotFoundException.class, ex.getClass());
    }

    @Test
    public void testValidaStock_ShouldBeFail() {

        var product = new Product();
        product.setStock(2);
        Exception ex = assertThrows(StockNotAvaibleException.class, () -> {
            this.orderService.validaStock(product, 10);
        });
        assertEquals("Quantidade solicitada do produto null está acima do estoque. Quantidade atual é 2", ex.getMessage());
        assertEquals(StockNotAvaibleException.class, ex.getClass());
    }

    @Test
    public void testCheckClientIsValid_ShouldBeFail() {

        var client = new ClientRequest();
        client.setIsActive(false);

        Exception ex = assertThrows(ClientNotActiveException.class, () -> {
            this.orderService.checkClientIsValid(client);
        });
        assertEquals("Cliente não está ativo e não pode realizar pedidos.", ex.getMessage());
        assertEquals(ClientNotActiveException.class, ex.getClass());
    }

    @Test
    public void testVerifyTokenEmpty_ShouldBeFail() {

        Exception ex = assertThrows(InvalidTokenException.class, () -> {
            this.orderService.verifyToken(new OrderRequest(), "");
        });
        assertEquals("The token must be informed.", ex.getMessage());
        assertEquals(InvalidTokenException.class, ex.getClass());
    }

    @Test
    public void testVerifyToken_ShouldBeFail() {

        Exception ex = assertThrows(ValidateDataException.class, () -> {
            this.orderService.verifyToken(new OrderRequest(), "123");
        });
        assertEquals("The e-mail of client must be informed.", ex.getMessage());
        assertEquals(ValidateDataException.class, ex.getClass());
    }

    private OrderRequest getRequest() {
        var newOrderRequest = new OrderRequest();
        newOrderRequest.setClientEmail("tiago@gmail.com");
        var newProduct = new ProductList();
        newProduct.setId("123");
        newProduct.setQuantity(3);
        List<ProductList> productList = new ArrayList<>(Arrays.asList(newProduct));
        newOrderRequest.setProductList(productList);
        return newOrderRequest;
    }

    private Order getOrder() {
        var order = new Order();
        order.setId(1);
        order.setClientEmail("tiago@gmail.com");
        return order;
    }

    private Product getProduct() {
        var product = new Product();
        product.setId("123");
        product.setPrice(7.98);
        product.setStock(100);
        return product;
    }

    private OrderRequest getOrderRequest() {
        var orderRequest = new OrderRequest();
        orderRequest.setClientEmail("tiago@gmail.com");
        return orderRequest;
    }

    private ClientRequest getClientRequest() {
        var clientRequest = new ClientRequest();
        clientRequest.setId(1);
        clientRequest.setEmail("tiago@gmail.com");
        clientRequest.setIsActive(true);
        var adress = new AdressRequest();
        adress.setCep("8754");
        clientRequest.setAdress(adress);
        return clientRequest;
    }

}
