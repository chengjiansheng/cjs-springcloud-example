package com.cjs.example.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ChengJianSheng
 * @date 2020-03-08
 */
@Data
public class RefreshRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String refreshToken;
}
