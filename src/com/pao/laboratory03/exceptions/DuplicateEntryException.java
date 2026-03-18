package com.pao.laboratory03.exceptions;

public class DuplicateEntryException extends RuntimeException{
    private final String message;

    public DuplicateEntryException(String message){
        super(message);
        this.message = message;
    }
    public String getMessage(){return message;}
}
