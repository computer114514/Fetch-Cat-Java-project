package com.github.computer114514.mapper;

import com.github.computer114514.domain.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User findUserByUsername(String username);
}
