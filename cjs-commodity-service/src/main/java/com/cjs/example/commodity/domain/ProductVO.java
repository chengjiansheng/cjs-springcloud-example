package com.cjs.example.commodity.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ChengJianSheng
 * @date 2019-06-27
 */
@Data
public class ProductVO implements Serializable {

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 现价
     */
    private BigDecimal actualPrice;

}
