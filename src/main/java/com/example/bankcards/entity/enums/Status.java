package com.example.bankcards.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum Status {
    CREATED,
    ACTIVE,
    EXPIRED,
    BLOCKED,
    DELETED
    ;
    @JsonCreator
    public static Status fromString(String value) {
        return Stream.of(Status.values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Такого статуса у карты не может быть: " + value));
    }
}
