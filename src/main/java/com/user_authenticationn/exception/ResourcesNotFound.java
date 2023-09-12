package com.user_authenticationn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourcesNotFound extends RuntimeException{


    public ResourcesNotFound(String msg){
        super(msg);
    }
}
