package com.github.computer114514.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //开始什锦锉，这是1970的时间戳罢了。
    private static final long BEGIN_TIMESTAMP=1640995200;
    //位移的数量
    private static final int COUNT_BITS=32;

    public long nextId(String keyPrefix){
        //1,时间戳。
        LocalDateTime now = LocalDateTime.now();
        long nowTimeStamp = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowTimeStamp - BEGIN_TIMESTAMP;
        //2,当前日期。
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //3,序列号。把不好康的日期格式化。
        Long count = stringRedisTemplate.opsForValue().increment("icr" + keyPrefix + ":" + date);
        //4，拼贴。
        return timeStamp<<COUNT_BITS | count;
    }

    public static void main(String[] args) {
        RedisIdWorker r=new RedisIdWorker();
        long id = r.nextId("test");
        System.out.println("id"+id);
    }
}
