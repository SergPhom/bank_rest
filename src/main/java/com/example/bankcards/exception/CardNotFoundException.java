package com.example.bankcards.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException() {
        super("Такая карта не найдена.");
    }
}
