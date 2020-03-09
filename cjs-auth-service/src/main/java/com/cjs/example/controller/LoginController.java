package com.cjs.example.controller;

import com.cjs.example.ResponseResult;
import com.cjs.example.domain.LoginRequest;
import com.cjs.example.domain.LoginResponse;
import com.cjs.example.domain.RefreshRequest;
import com.cjs.example.enums.ResponseCodeEnum;
import com.cjs.example.utils.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ChengJianSheng
 * @date 2020-03-08
 */
@RestController
public class LoginController {

    /**
     * Apollo 或 Nacos
     */
    @Value("${secretKey:123456}")
    private String secretKey;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 登录
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody @Validated LoginRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseResult.error(ResponseCodeEnum.PARAMETER_ILLEGAL.getCode(), ResponseCodeEnum.PARAMETER_ILLEGAL.getMessage());
        }

        String username = request.getUsername();
        String password = request.getPassword();
        //  假设查询到用户ID是1001
        String userId = "1001";
        if ("hello".equals(username) && "world".equals(password)) {
            //  生成Token
            String token = JWTUtil.generateToken(userId, secretKey);

            //  生成刷新Token
            String refreshToken = UUID.randomUUID().toString().replace("-", "");

            //  放入缓存
            HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
//            hashOperations.put(refreshToken, "token", token);
//            hashOperations.put(refreshToken, "user", username);
//            stringRedisTemplate.expire(refreshToken, JWTUtil.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

            /**
             * 如果可以允许用户退出后token如果在有效期内仍然可以使用的话，那么就不需要存Redis
             * 因为，token要跟用户做关联的话，就必须得每次都带一个用户标识，
             * 那么校验token实际上就变成了校验token和用户标识的关联关系是否正确，且token是否有效
             */

//            String key = MD5Encoder.encode(userId.getBytes());

            String key = userId;
            hashOperations.put(key, "token", token);
            hashOperations.put(key, "refreshToken", refreshToken);
            stringRedisTemplate.expire(key, JWTUtil.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setRefreshToken(refreshToken);
            loginResponse.setUsername(userId);

            return ResponseResult.success(loginResponse);
        }

        return ResponseResult.error(ResponseCodeEnum.LOGIN_ERROR.getCode(), ResponseCodeEnum.LOGIN_ERROR.getMessage());
    }

    /**
     * 退出
     */
    @GetMapping("/logout")
    public ResponseResult logout(@RequestParam("userId") String userId) {
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        String key = userId;
        hashOperations.delete(key);
        return ResponseResult.success();
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refreshToken")
    public ResponseResult refreshToken(@RequestBody @Validated RefreshRequest request, BindingResult bindingResult) {
        String userId = request.getUserId();
        String refreshToken = request.getRefreshToken();
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        String key = userId;
        String originalRefreshToken = hashOperations.get(key, "refreshToken");
        if (StringUtils.isBlank(originalRefreshToken) || !originalRefreshToken.equals(refreshToken)) {
            return ResponseResult.error(ResponseCodeEnum.REFRESH_TOKEN_INVALID.getCode(), ResponseCodeEnum.REFRESH_TOKEN_INVALID.getMessage());
        }

        //  生成新token
        String newToken = JWTUtil.generateToken(userId, secretKey);
        hashOperations.put(key, "token", newToken);
        stringRedisTemplate.expire(userId, JWTUtil.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return ResponseResult.success(newToken);
    }
}
