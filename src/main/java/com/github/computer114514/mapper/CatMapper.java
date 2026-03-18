package com.github.computer114514.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.computer114514.domain.enity.Cat;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CatMapper extends BaseMapper<Cat>{
}
