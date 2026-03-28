package com.example.bankcards.exception;

public class UserNotFoundException extends RuntimeException {
    private String details;
    public UserNotFoundException(String msg) {
        super("Пользователь не найден.");
        this.details = msg;
    }
}
