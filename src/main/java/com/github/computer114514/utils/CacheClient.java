package com.github.computer114514.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.computer114514.domain.dto.UserDTO;
import com.github.computer114514.domain.enity.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static cn.hutool.json.JSONUtil.toJsonStr;
import static com.github.computer114514.constant.RedisConstant.CACHE_NULL_TTL;
import static com.github.computer114514.constant.RedisConstant.REDIS_LOCK;

/**
 * 直译:缓存服务。
 * 用途:封装了缓存的工具类
 */
@Slf4j
@Component
public class CacheClient {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final ExecutorService CACHE_REBUILD_EXECUTOR =
            Executors.newFixedThreadPool(10);


    /**
     *普通的存缓存方法
     * @param key
     * @param value
     */
    public void set(String key, Object value, Long time, TimeUnit unit){
        String jsonStr = JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key,jsonStr,time,unit);
    }

    /**
     * 存储，同时logicalExpire，设计逻辑过期时间，但是事实上没有过期时间
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit){

//         * 通过redisData完成expireTime

        RedisData redisData = new RedisData();

        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(unit.toSeconds(time));
        redisData.setExpireTime(expireTime);

        redisData.setData(value);

        String jsonStr = JSONUtil.toJsonStr(redisData);
        stringRedisTemplate.opsForValue().set(key,jsonStr,time,unit);
    }

    /**
     * 防缓存穿透的请求方法-->缓存空值。
     * @param keyPrefix
     * @param id
     * @param type
     * @param dbFallback
     * @param time
     * @param unit
     * @return
     * @param <R>
     * @param <ID>
     */
    public <R,ID> R  queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID,R> dbFallback, Long time, TimeUnit unit){
        String key= keyPrefix + id;
        int userId = UserContext.getUser().getId();
        //1,redis存储缓存。
        String Json = stringRedisTemplate.opsForValue().get(key);
        //2,判断存在//3,直接返回
        if(StrUtil.isNotBlank(Json)){
            return JSONUtil.toBean(Json, type);
        }
        //2,加一种情况，是空值，所以不麻烦数据库大人了。
        if (Objects.equals(Json, "")) {
            return null;
        }
        //4，数据库,需要传递
        R r = dbFallback.apply(id);
        //5，不存在401
        if (r==null) {
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        //6,写入redis，返回
        this.set(key,r,time,unit);

        return r;
    }

    public <R,ID> R queryWithLogicalExpire(String keyPrefix,ID id,Class<R> type,Function<ID,R> dbFallBack, Long time, TimeUnit unit ) {
        String key=keyPrefix  + id;
        //1,redis存储缓存。s
        String json = stringRedisTemplate.opsForValue().get(key);
        //2,判断存在//3,直接返回
        if(StrUtil.isBlank(json)){
            return null;
        }
        //4,命中，需要json反序列化对象，判断是否过期
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        JSONObject data =(JSONObject) redisData.getData();//未指定的object
        R r = JSONUtil.toBean(data, type);
        LocalDateTime expireTime = redisData.getExpireTime();

        if(expireTime.isAfter(LocalDateTime.now())){
            //4.1,未过期
            return r;
        }
        //4.1,过期,缓存重建。
        String lockKey=REDIS_LOCK+id;
        //5.1获取锁--->失败,返回旧数据
        boolean isLock = tryLock(lockKey);
        if(isLock){
            //5.2成功，开启新线程,缓存重建。
            CACHE_REBUILD_EXECUTOR.submit(()->{
                try {
                    //查数据库
                    R r1 = dbFallBack.apply(id);
                    //缓存重置
                    this.setWithLogicalExpire(key,r1,time,unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }finally {
                    unLock(lockKey);
                }
            });
        }
        return r;
    }

    /**
     * 获取互斥锁-----缓存击穿
     * @param key
     * @return
     */
    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 开互斥锁
     * @param key
     */
    private void unLock(String key){
        stringRedisTemplate.delete(key);
    }
}
















