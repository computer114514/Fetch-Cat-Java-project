package com.github.computer114514.domain.enity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("rareCat")
public class RareCat {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private  String id;
    private  String name;
    private  String url;
    private Integer store;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
}


