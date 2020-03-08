package com.cjs.example.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ChengJianSheng
 * @date 2020-03-08
 */
@Data
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
