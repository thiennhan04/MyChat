package com.project.mychat.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class UserException extends Exception{
    public UserException(String message){
        super(message);
    }
}
