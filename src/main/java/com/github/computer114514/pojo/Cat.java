package com.github.computer114514.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Cat {
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