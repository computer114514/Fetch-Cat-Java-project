package com.github.computer114514.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private static final ThreadLocal<Long> USER_ID_HOLDER=new ThreadLocal<>();
    //这就是线程

    public static void setId(Long userId){
        USER_ID_HOLDER.set(userId);
    }

    public static Long getId(){
        return USER_ID_HOLDER.get();
    }

    public static void clear(Long userId){
        USER_ID_HOLDER.remove();
    }
}
