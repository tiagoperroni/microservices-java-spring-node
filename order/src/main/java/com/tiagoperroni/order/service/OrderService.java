package com.tiagoperroni.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.tiagoperroni.order.exceptions.ClientNotActiveException;
import com.tiagoperroni.order.exceptions.StockNotAvaibleException;
import com.tiagoperroni.order.feign.ClientFeignRequest;
import com.tiagoperroni.order.model.Client;
import com.tiagoperroni.order.model.OrderItems;
import com.tiagoperroni.order.model.OrderRequest;
import com.tiagoperroni.order.model.OrderResponse;
import com.tiagoperroni.order.model.Product;
import com.tiagoperroni.order.model.ProductList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private ClientFeignRequest clientFeignRequest;

    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * Realiza a criação de um novo pedido
     * @param orderRequest
     * @return
     */

    public OrderResponse makeOrder(OrderRequest orderRequest) {
        logger.info("Recebendo novo pedido: {}", orderRequest);
        var orderResponse = new OrderResponse();
        orderResponse.setId(UUID.randomUUID().toString());
        orderResponse.setClient(this.getClientRequest(orderRequest.getClientId()));
        var orderItems = this.prepareOrder(orderRequest);
        orderResponse.setItems(orderItems);
        orderResponse.setQuantityTotal(this.totalQuantity(orderItems));
        orderResponse.setTotalPrice(this.formatDouble(orderItems));
        orderResponse.setOrderDate(LocalDateTime.now());
        return orderResponse;
    }

    /**
     * Requisição GET para api de Clientes por id pelo RestTemplate
     * @param id
     * @param quantity
     * @return
     */

    public Product getProductRequest(int id, int quantity) {
        logger.info("Enviando requisição para API PRODUTOS");
        String urlProductApi = "http://localhost:3232/product/" + id + "/" + quantity;
        ResponseEntity<Product[]> request = new RestTemplate().getForEntity(urlProductApi, Product[].class);
        logger.info("Recebendo dados da API PRODUTOS: {}", request.getBody()[0]);
        return request.getBody()[0];

    }

    /**
     * Requisição GET para api de Clientes por id pelo Open Feign
     * @param id
     * @return
     */
    
    public Client getClientRequest(int id) {
        logger.info("Enviando requisição para API CLIENTES");
        Client client = clientFeignRequest.getClient(id).getBody();
        logger.info("Recebendo dados da API CLIENTES: {}", client);
        this.checkClientIsValid(client);
        return client;

    }

    /**
     * Prepara a lista de produtos comprados pelo cliente
     * @param request
     * @return
     */

    public List<OrderItems> prepareOrder(OrderRequest request) {
        var orderItems = new ArrayList<OrderItems>();
        for (ProductList product : request.getProductList()) {
            var productResponse = this.getProductRequest(product.getId(), request.getQuantity());
            this.validaStock(productResponse, request);
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
     * @param product
     * @param request
     */

    public void validaStock(Product product, OrderRequest request) {
        if (product.getStock() < request.getQuantity()) {
            throw new StockNotAvaibleException(
                    "Quantidade acima do estoque do produto. Quantidade atual: " + product.getStock());
        }
    }

    /**
     * verifica se o cliente está ativo
     * @param client
     */

    public void checkClientIsValid(Client client) {
        if (!client.getIsActive()) {
            throw new ClientNotActiveException("Cliente não está ativo e não pode realizar pedidos.");
        }
    }

    /**
     * 
     * @param orderItems
     * @return total do pedido calculado
     */

    public Double formatDouble(List<OrderItems> orderItems) {
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

    public Integer totalQuantity(List<OrderItems> orderItems) {
        int quantity = 0;   
        for (OrderItems orderItem : orderItems) {
            quantity += orderItem.getQuantity();          
        }
        return quantity;
    }
}
