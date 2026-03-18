package com.github.computer114514.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.github.computer114514.domain.dto.UserDTO;
import com.github.computer114514.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.computer114514.constant.UserConstant.REDIS_USER_LOGIN_TOKEN_DDL;
import static com.github.computer114514.constant.UserConstant.REDIS_USER_LOGIN_TOKEN_KEY;

public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        //1,获得前端传过来的token
        if(token==null||!token.startsWith("Bearer ")){
            return true;
            //不拦截，纯刷新
        }
        token = token.substring(7);//去掉bearer
//2,检查token有效性。
//        if(!JWTUtil.validateToken(token)){
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("未授权,token不合法");
//            return false;
//        }
//        //3,解析token
//        Claims claims = JWTUtil.parseToken(token);
//        Long userId = claims.get("userId", Long.class);
//        if(userId==null){
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("token对应的userId为无效！");
//        }
        String key = REDIS_USER_LOGIN_TOKEN_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        if(userMap.isEmpty()){
            return true;
        }
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);

        UserContext.setUser(userDTO);

        //更新token,
        stringRedisTemplate.expire(key,REDIS_USER_LOGIN_TOKEN_DDL, TimeUnit.MINUTES);

        return true;
        //4，放行
    }
}
