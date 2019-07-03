package com.cjs.example.price.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author ChengJianSheng
 * @date 2019-07-02
 */
@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/test01")
    public String test01(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info(headerName + " : " + headerValue);
        }
        return "ok";
    }

}
