package com.tiagoperroni.order.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagoperroni.order.exceptions.ClientNotActiveException;
import com.tiagoperroni.order.exceptions.ClientNotFoundException;
import com.tiagoperroni.order.exceptions.InvalidTokenException;
import com.tiagoperroni.order.exceptions.StockNotAvaibleException;
import com.tiagoperroni.order.exceptions.ValidateDataException;
import com.tiagoperroni.order.feign.ClientFeignRequest;
import com.tiagoperroni.order.feign.ProductFeignRequest;
import com.tiagoperroni.order.mapper.OrderMapper;
import com.tiagoperroni.order.models.ClientRequest;
import com.tiagoperroni.order.models.OrderItems;
import com.tiagoperroni.order.models.OrderRequest;
import com.tiagoperroni.order.models.OrderResponse;
import com.tiagoperroni.order.models.Product;
import com.tiagoperroni.order.models.ProductList;
import com.tiagoperroni.order.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private ClientFeignRequest clientFeignRequest;

    @Autowired
    private ProductFeignRequest productFeignRequest;

    @Autowired
    private ClientLoginService clientLoginService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMessageService orderMessageService;

    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * Realiza a criação de um novo pedido
     * 
     * @param orderRequest
     * @return
     * @throws JsonProcessingException
     */

    public OrderResponse makeOrder(OrderRequest orderRequest, String token) {
        logger.info("OrderService - Recebendo novo pedido: {}", orderRequest.getClientEmail());

        this.verifyToken(orderRequest, token);

        var orderResponse = new OrderResponse();
        orderResponse.setClient(this.getClientRequest(orderRequest.getClientEmail()));

        var orderItems = this.prepareOrder(orderRequest);
        orderResponse.setItems(orderItems);
        orderResponse.setQuantityTotal(this.totalQuantity(orderItems));
        orderResponse.setTotalPrice(this.formatDouble(orderItems));
        orderResponse.setOrderDate(LocalDate.now().toString());

        logger.info("OrderService - Salvando novo pedido no DB");

        var order = this.orderRepository.save(OrderMapper.convertFromResponse(orderResponse));

        orderResponse.setId(order.getId());

        this.orderMessageService.sendOrderMessage(orderResponse);

        logger.info("OrderService - Enviando detalhes do pedido");
        return orderResponse;
    }

    /**
     * Requisição GET para api de Clientes por id pelo Open Feign
     * 
     * @param id
     * @return
     */
    public ClientRequest getClientRequest(String email) {
        logger.info("Enviando requisição para API CLIENTES");
        ClientRequest client = clientFeignRequest.getClient(email).getBody();
        if (client != null) {
            logger.info("Recebendo dados da API CLIENTES: {}", client);
            this.checkClientIsValid(client);
            return client;
        }

        throw new ClientNotFoundException(String.format("Client with e-mail %s not was found.", email));
    }

    /**
     * Requisição GET para api de Products por id pelo Open Feign
     * 
     * @param id
     * @param quantity
     * @return
     */

    public Product getProductRequest(String id) {
        logger.info("Enviando requisição para API PRODUTOS");
        Product responseProduct = this.productFeignRequest.getProductById(id).getBody();
        logger.info("Recebendo dados da API PRODUTOS: {}", responseProduct);
        return responseProduct;
    }

    public void updateStockRequest(String id, int quantity) {
        logger.info("Enviando requisição para API PRODUTOS");
        this.productFeignRequest.updateStockRequest(id, quantity);
        logger.info("Recebendo dados da API PRODUTOS: {}");
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
            var productResponse = this.getProductRequest(product.getId());
            this.validaStock(productResponse, product.getQuantity());
            var orderItem = new OrderItems();
            orderItem.setProductName(productResponse.getName());
            orderItem.setProductPrice(productResponse.getPrice());
            orderItem.setQuantity(product.getQuantity());
            orderItem.setTotal(this.formatTotalPriceProduct(product.getQuantity() * productResponse.getPrice()));
            orderItems.add(orderItem);
        }
        for (ProductList product : request.getProductList()) {
            this.updateStockRequest(product.getId(), product.getQuantity());
        }

        return orderItems;
    }

    /**
     * Valida se existe estoque disponível
     * 
     * @param product
     */

    public void validaStock(Product product, int productRequest) {
        if (product.getStock() >= productRequest) {             
        } else {
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

    public void checkClientIsValid(ClientRequest client) {
        if (!client.getIsActive()) {
            throw new ClientNotActiveException("Cliente não está ativo e não pode realizar pedidos.");
        }
    }

    /**
     * 
     * @param orderItems
     * @return formata total do pedido calculado
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
     * Formata o valor total dentro da lista de produtos
     * @param total
     * @return
     */

    public Double formatTotalPriceProduct(Double total) {
        String totalConverted = String.format("%.2f", total);
        return Double.parseDouble(totalConverted.replace(",", "."));
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
     * 
     * @param orderRequest
     * @param token
     */

    public void verifyToken(OrderRequest orderRequest, String token) {
        if (token.equals(null) || token.isEmpty()) {
            throw new InvalidTokenException("The token must be informed.");
        } else if (orderRequest.getClientEmail() == null) {
            throw new ValidateDataException("The e-mail of client must be informed.");
        } else {
            this.clientLoginService.verifyTokenIsValid(orderRequest, token);
        }
    }
}
