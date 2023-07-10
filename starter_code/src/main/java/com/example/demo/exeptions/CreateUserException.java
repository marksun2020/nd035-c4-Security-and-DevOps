package com.example.demo.exeptions;

public class CreateUserException extends RuntimeException {
    private String userName;
    public CreateUserException(String userName, String message) {
        super(message);
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }
}
