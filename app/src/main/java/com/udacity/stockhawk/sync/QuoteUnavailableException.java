package com.udacity.stockhawk.sync;

public class QuoteUnavailableException extends Exception {
    public QuoteUnavailableException(String message) {
        super(message);
    }
}
