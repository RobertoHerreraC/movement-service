package com.bank.movement.exception;

public class MovementNotFoundException extends RuntimeException {
    public MovementNotFoundException(String id) {
        super("Movement not found with id: " + id);
    }
}