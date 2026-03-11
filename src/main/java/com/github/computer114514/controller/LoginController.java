package com.github.computer114514.controller;

import com.github.computer114514.domain.dto.loginFromDTO;
import com.github.computer114514.domain.pojo.User;
import com.github.computer114514.pojo.Result;
import com.github.computer114514.utils.JWTUtil;
import com.github.computer114514.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody loginFromDTO loginFromDTO) {
        //1,获取username
        String username = loginFromDTO.getUsername();
        //2,根据username查找用户
        User user=userService.findUserByUsername(username);
        //3，不存在或者密码错误报错
        if(user==null||!user.getPassword().equals(loginFromDTO.getPassword())){
            return Result.error(401,"用户不存在或者密码错误！");
        }
        //4，密码正确，生产token
        String token = JWTUtil.generateToken(username, (long) user.getId());
        //5,返回数据
        Map<String,String> map=new HashMap<>();
        map.put("token",token);
        return Result.success(map);
    }
}
