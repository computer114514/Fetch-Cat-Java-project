package com.github.computer114514.controller;

import com.github.computer114514.domain.dto.loginFromDTO;
import com.github.computer114514.domain.vo.LoginVO;
import com.github.computer114514.domain.enity.Result;
import com.github.computer114514.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping("/login")
    public Result<LoginVO> passwordLogin(@RequestBody loginFromDTO loginFromDTO) {
       return userService.passwordLogin(loginFromDTO);
    }
    @PostMapping("/codeLogin")
    public Result<LoginVO> codeLogin(@RequestBody loginFromDTO loginFromDTO) {
       return userService.codeLogin(loginFromDTO);
    }
    @PostMapping("/sendCode")
    public Result<Void> sendCode(@RequestBody String phone) {
        return userService.sendCode(phone);
    }
}
