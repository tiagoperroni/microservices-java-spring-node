package com.tiagoperroni.order.service;

import com.tiagoperroni.order.exceptions.ClientNotActiveException;
import com.tiagoperroni.order.exceptions.StockNotAvaibleException;
import com.tiagoperroni.order.feign.ClientFeignRequest;
import com.tiagoperroni.order.feign.ProductFeignRequest;
import com.tiagoperroni.order.model.Client;
import com.tiagoperroni.order.model.OrderRequest;
import com.tiagoperroni.order.model.Product;
import com.tiagoperroni.order.model.ProductList;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

class OrderServiceTest {

    public static final String URL_SERVER_PRODUCT = "http://localhost:3000/product/1/2";
    @InjectMocks
    private OrderService orderService;

    @Mock
    private ClientFeignRequest clientFeignRequest;

    @Mock
    private ProductFeignRequest productFeignRequest;

    //@Mock
    //private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMakeOrder_Success() {

        ProductList productList = new ProductList(1, 2);
        List<ProductList> productListList = new ArrayList<>();
        productListList.add(productList);
        OrderRequest request = new OrderRequest(productListList, 1);
        request.setClientId(1);
        request.setClientId(1);

        Client client = new Client(1, "Tiago Perroni", "054.659.789-78", true, null);
        when(this.clientFeignRequest.getClient(anyInt())).thenReturn(new ResponseEntity<Client>(client, HttpStatus.OK));

        Product product = new Product(1, "Refri Cola", 7.98, 15);
        when(this.productFeignRequest.getProductById(anyInt(), anyInt())).thenReturn(new ResponseEntity<Product>(product, HttpStatus.OK));

        //when(this.restTemplate.getForEntity(URL_SERVER_PRODUCT, Product.class))
                //.thenReturn(new ResponseEntity<Product>(new Product(1, "Refri Cola", 7.98, 15), HttpStatus.OK));

        var orderResponse = this.orderService.makeOrder(request);

        assertEquals("Tiago Perroni", orderResponse.getClient().getName());
        assertEquals("Refri Cola", orderResponse.getItems().get(0).getProductName());
    }

    @Test
    public void testMakeOrder_Fail_OutOfStock() {

        ProductList productList = new ProductList(1, 20);
        List<ProductList> productListList = new ArrayList<>();
        productListList.add(productList);
        OrderRequest request = new OrderRequest(productListList, 1);

        Client client = new Client(1, "Tiago Perroni", "054.659.789-78", true, null);
        when(this.clientFeignRequest.getClient(anyInt())).thenReturn(new ResponseEntity<Client>(client, HttpStatus.OK));

        Product product = new Product(1, "Refri Cola", 7.98, 15);
        when(this.productFeignRequest.getProductById(anyInt(), anyInt())).thenReturn(new ResponseEntity<Product>(product, HttpStatus.OK));

        //when(this.restTemplate.getForEntity("http://localhost:3000/product/1/20", Product.class))
                //.thenReturn(new ResponseEntity<Product>(new Product(1, "Refri Cola", 7.98, 15), HttpStatus.OK));

        Exception exception = assertThrows(StockNotAvaibleException.class, () ->
               this.orderService.makeOrder(request));

        assertEquals("Quantidade solicitada do produto Refri Cola está acima do estoque. Quantidade atual é 15", exception.getMessage());
        assertEquals(StockNotAvaibleException.class, exception.getClass());
    }

    @Test
    public void testMakeOrder_Fail_ClientIsNotActive() {

        ProductList productList = new ProductList(1, 4);
        List<ProductList> productListList = new ArrayList<>();
        productListList.add(productList);
        OrderRequest request = new OrderRequest(productListList, 1);
        request.setClientId(1);
        request.setClientId(1);

        Client client = new Client(1, "Tiago Perroni", "054.659.789-78", false, null);
        when(this.clientFeignRequest.getClient(anyInt())).thenReturn(new ResponseEntity<Client>(client, HttpStatus.OK));

        Exception exception = assertThrows(ClientNotActiveException.class, () ->
                this.orderService.makeOrder(request));
        assertEquals("Cliente não está ativo e não pode realizar pedidos.", exception.getMessage());
        assertEquals(ClientNotActiveException.class, exception.getClass());
    }
}
