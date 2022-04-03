    package com.tiagoperroni.order.builder;

    import com.tiagoperroni.order.models.Order;
    import com.tiagoperroni.order.models.OrderItems;

    import java.util.List;

    public class OrderBuilder {

        private Integer id;
        private String clientName;
        private String clientCpf;
        private String clientEmail;
        private String cep;
        private String numberOfHouse;
        private List<OrderItems> items;
        private int quantityTotal;
        private Double totalPrice;
        private String orderDate;

        public OrderBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public OrderBuilder clientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public OrderBuilder clientCpf(String clientCpf) {
            this.clientCpf = clientCpf;
            return this;
        }

        public OrderBuilder clientEmail(String clientEmail) {
            this.clientEmail = clientEmail;
            return this;
        }

        public OrderBuilder cep(String cep) {
            this.cep = cep;
            return this;
        }

        public OrderBuilder numberOfHouse(String numberOfHouse) {
            this.numberOfHouse = numberOfHouse;
            return this;
        }

        public OrderBuilder items(List<OrderItems> items) {
            this.items = items;
            return this;
        }

        public OrderBuilder quantityTotal(Integer quantityTotal) {
            this.quantityTotal = quantityTotal;
            return this;
        }

        public OrderBuilder totalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderBuilder orderDate(String orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Order build() {
            return new Order(id, clientName, clientCpf, clientEmail, cep, numberOfHouse,
                        items, quantityTotal, totalPrice, orderDate);
        }
    }
