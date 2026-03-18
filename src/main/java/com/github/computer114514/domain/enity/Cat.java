package com.github.computer114514.domain.enity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@Data
@NoArgsConstructor
@TableName("cat")
public class Cat {
    @TableId(value="id",type= IdType.AUTO)
    private Long id;

    private Long userId;

    private String catId;

    private String name;

    private String imageUrl;

    private Integer age;

    private Integer favorability;

    private String favorabilityLevel;

    private Integer petCount;
    //摸摸次数
    private Integer walkCount;

    private Date catchTime;
}