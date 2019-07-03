package com.cjs.example.commodity.controller;

import com.cjs.example.commodity.domain.ProductVO;
import com.cjs.example.commodity.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChengJianSheng
 * @date 2019-06-27
 */
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 商品详情
     * @param productId
     * @return
     */
    @GetMapping("/detail/{productId}")
    public ProductVO detail(@PathVariable("productId") Integer productId) {
        log.info("[商品管理][商品详情]-收到请求：productId={}", productId);
        return productService.getById(productId);
    }

}
