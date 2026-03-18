package com.github.computer114514.domain.enity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 击穿特供redisdata...
 * 用于逻辑过期
 */
@Data

public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}
