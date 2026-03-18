package com.github.computer114514.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.computer114514.domain.dto.loginFromDTO;
import com.github.computer114514.domain.enity.User;
import com.github.computer114514.domain.vo.LoginVO;
import com.github.computer114514.domain.enity.Result;

public interface UserService extends IService<User> {
    User findUserByUsername(String username);

    Result<LoginVO> passwordLogin(loginFromDTO loginFromDTO);

    Result<Void> sendCode(String phone);

    Result<LoginVO> codeLogin(loginFromDTO loginFromDTO);
}

