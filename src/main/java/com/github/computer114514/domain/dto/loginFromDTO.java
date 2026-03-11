package com.github.computer114514.domain.dto;

import lombok.Data;

@Data
public class loginFromDTO {

    /**
     * 用户名(账号)
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
