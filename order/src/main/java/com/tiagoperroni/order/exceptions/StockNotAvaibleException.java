package com.tiagoperroni.order.exceptions;

public class StockNotAvaibleException extends RuntimeException {
    
    public StockNotAvaibleException(String msg) {
        super(msg);
    }
}
