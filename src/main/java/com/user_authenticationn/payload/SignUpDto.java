package com.user_authenticationn.payload;

import lombok.Data;

@Data
public class SignUpDto {
    private long id;
    private String name;
    private String username;
    private String email;
    private String password;

}

