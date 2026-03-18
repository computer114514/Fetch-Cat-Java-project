package com.github.computer114514.utils;

import com.github.computer114514.domain.dto.UserDTO;
import org.springframework.stereotype.Component;

/**
 * 获取，存储threadlocal
 */
@Component
public class UserContext {
    private static final ThreadLocal<UserDTO> USER_ID_HOLDER=new ThreadLocal<>();
    //这就是线程

    public static void setUser(UserDTO userDTO){
        USER_ID_HOLDER.set(userDTO);
    }

    public static UserDTO getUser(){
        return USER_ID_HOLDER.get();
    }

    public static void clear(Long userId){
        USER_ID_HOLDER.remove();
    }
}
