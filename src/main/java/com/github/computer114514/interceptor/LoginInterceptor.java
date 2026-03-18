package com.github.computer114514.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.github.computer114514.domain.dto.UserDTO;
import com.github.computer114514.utils.JWTUtil;
import com.github.computer114514.utils.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.computer114514.constant.UserConstant.REDIS_USER_LOGIN_TOKEN_DDL;
import static com.github.computer114514.constant.UserConstant.REDIS_USER_LOGIN_TOKEN_KEY;

@Component
public class LoginInterceptor implements HandlerInterceptor {


    /**
      拦截器:1,前端传进来request
     * 2,获取token，不存在死。
     * 3，redis查询token,看有没有登录凭证，没有，死！
     * 4，存储到threadLocal
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO user = UserContext.getUser();
        if(user==null){
            response.setStatus(401);
        }
        return user != null;
    }
}
