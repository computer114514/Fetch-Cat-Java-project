package com.github.computer114514.service.Impl;

import com.github.computer114514.domain.pojo.User;
import com.github.computer114514.mapper.UserMapper;
import com.github.computer114514.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User findUserByUsername(String username) {
       return  userMapper.findUserByUsername(username);
    }
}
