package com.cjs.example.commodity.service.impl;

import com.cjs.example.commodity.domain.ProductVO;
import com.cjs.example.commodity.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author ChengJianSheng
 * @date 2019-06-27
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public ProductVO getById(Integer id) {
        ProductVO productVO = new ProductVO();
        productVO.setProductId(id);
        productVO.setProductName("荣耀V20");
        productVO.setOriginalPrice(new BigDecimal("2999"));
        productVO.setActualPrice(new BigDecimal("2599"));
        return productVO;
    }
}
