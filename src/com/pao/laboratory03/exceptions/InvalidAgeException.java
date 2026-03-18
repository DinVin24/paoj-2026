package com.pao.laboratory03.exceptions;

public class InvalidAgeException extends RuntimeException{
    private final String message;

    public InvalidAgeException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){return message;}

}
