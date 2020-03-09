package com.cms.example.filter;

import com.alibaba.fastjson.JSON;
import com.cjs.example.ResponseResult;
import com.cjs.example.enums.ResponseCodeEnum;
import com.cjs.example.exception.TokenAuthenticationException;
import com.cjs.example.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author ChengJianSheng
 * @date 2020-03-08
 */
@Slf4j
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Value("${secretKey:123456}")
    private String secretKey;

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        String uri = serverHttpRequest.getURI().getPath();

        //  检查白名单（配置）
        if (uri.indexOf("/auth-server/login") >= 0) {
            return chain.filter(exchange);
        }

        String token = serverHttpRequest.getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)) {
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_MISSION);
        }

        //todo 检查Redis中是否有此Token

        try {
            JWTUtil.verifyToken(token, secretKey);
        } catch (TokenAuthenticationException ex) {
            return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_INVALID);
        } catch (Exception ex) {
            return getVoidMono(serverHttpResponse, ResponseCodeEnum.UNKNOWN_ERROR);
        }

        String userId = JWTUtil.getUserInfo(token);

        ServerHttpRequest mutableReq = serverHttpRequest.mutate().header("userId", userId).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();

        return chain.filter(mutableExchange);
    }

    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, ResponseCodeEnum responseCodeEnum) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        ResponseResult responseResult = ResponseResult.error(responseCodeEnum.getCode(), responseCodeEnum.getMessage());
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONString(responseResult).getBytes());
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
