package com.github.computer114514.interceptor;

import com.github.computer114514.utils.JWTUtil;
import com.github.computer114514.utils.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class LoginInterceptor implements HandlerInterceptor {

    //拦截器:

    @Override

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        //1,获得前端传过来的token
        if(token==null||!token.startsWith("Bearer ")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("未授权,token无效");
            return false;
        }
        token = token.substring(7);//去掉bearer
//2,检查token有效性。
        if(!JWTUtil.validateToken(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("未授权,token不合法");
            return false;
        }
        //3,解析token
        Claims claims = JWTUtil.parseToken(token);
        Long userId = claims.get("userId", Long.class);
        if(userId==null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("token对应的userId为无效！");
        }
        UserContext.setId(userId);

        return true;
        //4，放行
    }
}
