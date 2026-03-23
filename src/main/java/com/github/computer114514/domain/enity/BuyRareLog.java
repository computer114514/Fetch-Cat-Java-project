package com.github.computer114514.domain.enity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

@Data
@TableName("buy_rare_log")
public class BuyRareLog {
    @TableId(value = "id", type = IdType.AUTO)
    private long id;
    private Integer userId;
    private LocalDateTime operationTime;
    private String des;
}
