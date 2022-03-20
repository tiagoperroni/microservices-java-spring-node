package com.tiagoperroni.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tiagoperroni.order.exceptions.ClientNotActiveException;
import com.tiagoperroni.order.exceptions.ClientNotFoundException;
import com.tiagoperroni.order.exceptions.InvalidTokenException;
import com.tiagoperroni.order.exceptions.StockNotAvaibleException;
import com.tiagoperroni.order.feign.ClientFeignRequest;
import com.tiagoperroni.order.feign.ProductFeignRequest;
import com.tiagoperroni.order.model.ClientRequest;
import com.tiagoperroni.order.model.OrderItems;
import com.tiagoperroni.order.model.OrderRequest;
import com.tiagoperroni.order.model.OrderResponse;
import com.tiagoperroni.order.model.Product;
import com.tiagoperroni.order.model.ProductList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private ClientFeignRequest clientFeignRequest;

    @Autowired
    private ProductFeignRequest productFeignRequest;

    @Autowired
    private ClientLoginService clientLoginService;

    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * Realiza a criação de um novo pedido
     * 
     * @param orderRequest
     * @return
     */

    public OrderResponse makeOrder(OrderRequest orderRequest, String token) {
        logger.info("Recebendo novo pedido: {}", orderRequest);   
            this.verifyToken(orderRequest, token);    
            var orderResponse = new OrderResponse();
            orderResponse.setId(UUID.randomUUID().toString());
            orderResponse.setClient(this.getClientRequest(orderRequest.getClientId()));
            var orderItems = this.prepareOrder(orderRequest);
            orderResponse.setItems(orderItems);
            orderResponse.setQuantityTotal(this.totalQuantity(orderItems));
            orderResponse.setTotalPrice(this.formatDouble(orderItems));
            orderResponse.setOrderDate(LocalDateTime.now());
            logger.info("Enviando detalhes do pedido: {}", orderResponse);
            return orderResponse;
        
    }

    // /**
    // * Requisição GET para api de Clientes por id pelo RestTemplate
    // *
    // * @param id
    // * @param quantity
    // * @return
    // */

    // public Product getProductRequest(int id, int quantity) {
    // logger.info("Enviando requisição para API PRODUTOS");

    // String urlProductApi = "http://localhost:3000/product/" + id + "/" +
    // quantity;
    // ResponseEntity<Product> request = this.restTemplate.getForEntity(urlProductApi, Product.class);
    // logger.info("Recebendo dados da API PRODUTOS: {}", request.getBody());
    // return request.getBody();
    // }

    /**
     * Requisição GET para api de Clientes por id pelo Open Feign
     * 
     * @param id
     * @return
     */
    public ClientRequest getClientRequest(int id) {
        logger.info("Enviando requisição para API CLIENTES");
        ClientRequest client = clientFeignRequest.getClient(id).getBody();
        if (client != null) {
        logger.info("Recebendo dados da API CLIENTES: {}", client);
        this.checkClientIsValid(client);
        return client;
    }
        
        throw new ClientNotFoundException(String.format("Client with id %s not was found.", id));
    }

    /**
     * Requisição GET para api de Products por id pelo Open Feign
     * 
     * @param id
     * @param quantity
     * @return
     */

    public Product getProductRequest(int id, int quantity) {
        logger.info("Enviando requisição para API PRODUTOS");
        Product responseProduct = this.productFeignRequest.getProductById(id, quantity).getBody();
        logger.info("Recebendo dados da API PRODUTOS: {}", responseProduct);
        return responseProduct;
    }

    /**
     * Prepara a lista de produtos comprados pelo cliente
     * 
     * @param request
     * @return
     */

    public List<OrderItems> prepareOrder(OrderRequest request) {
        var orderItems = new ArrayList<OrderItems>();
        for (ProductList product : request.getProductList()) {
            var productResponse = this.getProductRequest(product.getId(), product.getQuantity());
            this.validaStock(productResponse, product);
            var orderItem = new OrderItems();
            orderItem.setProductId(productResponse.getId());
            orderItem.setProductName(productResponse.getName());
            orderItem.setProductPrice(productResponse.getPrice());
            orderItem.setQuantity(product.getQuantity());
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    /**
     * Valida se existe estoque disponível
     * 
     * @param product
     */

    private void validaStock(Product product, ProductList productList) {
        if (product.getStock() < productList.getQuantity()) {
            throw new StockNotAvaibleException(
                    String.format("Quantidade solicitada do produto %s está acima do estoque. Quantidade atual é %s",
                            product.getName(), product.getStock()));
        }
    }

    /**
     * verifica se o cliente está ativo
     * 
     * @param client
     */

    private void checkClientIsValid(ClientRequest client) {
        if (!client.getIsActive()) {
            throw new ClientNotActiveException("Cliente não está ativo e não pode realizar pedidos.");
        }
    }

    /**
     * 
     * @param orderItems
     * @return total do pedido calculado
     */

    private Double formatDouble(List<OrderItems> orderItems) {
        Double total = 0.0;
        String totalConvered = null;
        for (OrderItems orderItem : orderItems) {
            total += orderItem.getProductPrice() * orderItem.getQuantity();
            totalConvered = String.format("%.2f", total);
        }
        return Double.parseDouble(totalConvered.replace(",", "."));
    }

    /**
     * 
     * @param orderItems
     * @return a quantidade total do pedido
     */

    private Integer totalQuantity(List<OrderItems> orderItems) {
        int quantity = 0;
        for (OrderItems orderItem : orderItems) {
            quantity += orderItem.getQuantity();
        }
        return quantity;
    }

    /**
     * verificando se token foi informado
     * @param orderRequest
     * @param token
     */

    public void verifyToken(OrderRequest orderRequest, String token) {
        if (token.equals(null)) {
            throw new InvalidTokenException("The token must be informed.");
        } else {
            this.clientLoginService.verifyTokenIsValid(orderRequest, token);
        }
    }
   
}
