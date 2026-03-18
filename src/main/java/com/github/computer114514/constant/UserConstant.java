package com.github.computer114514.constant;

public class UserConstant {
    public UserConstant(){
        throw new RuntimeException("不要实例化常量类");
    }
    public static final String REDIS_USER_LOGIN_TOKEN_KEY="user:login:";
    public static final Long REDIS_USER_LOGIN_TOKEN_DDL=36000L;
}
