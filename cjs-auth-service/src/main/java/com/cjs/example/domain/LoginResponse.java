package com.cjs.example.domain;

import lombok.Data;

/**
 * @author ChengJianSheng
 * @date 2020-03-08
 */
@Data
public class LoginResponse {

    private String token;

    private String refreshToken;

    private String username;
}
