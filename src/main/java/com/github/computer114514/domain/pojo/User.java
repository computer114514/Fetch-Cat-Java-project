package com.github.computer114514.domain.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private LocalDateTime lastLoginIn;
}
