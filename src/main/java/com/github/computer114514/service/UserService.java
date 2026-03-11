package com.github.computer114514.service;

import com.github.computer114514.domain.pojo.User;

public interface UserService {
    User findUserByUsername(String username);
}
