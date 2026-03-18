package com.github.computer114514.domain.enity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(value="id",type = IdType.AUTO)
    private int id;
    private String username;
    private String password;
    private LocalDateTime lastLoginIn;
    private String phone;
}
