package com.project.mychat.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor

public class MessageException extends Exception{
    public MessageException(String message){
        super(message);
    }
}
