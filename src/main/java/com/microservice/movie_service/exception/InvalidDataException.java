package com.microservice.movie_service.exception;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message){
        super(message);
    }
}
