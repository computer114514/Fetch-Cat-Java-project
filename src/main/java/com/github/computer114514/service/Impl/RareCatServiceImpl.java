package com.github.computer114514.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.computer114514.domain.dto.UserDTO;
import com.github.computer114514.domain.enity.BuyRareLog;
import com.github.computer114514.domain.enity.Cat;
import com.github.computer114514.domain.enity.RareCat;
import com.github.computer114514.domain.enity.Result;
import com.github.computer114514.domain.vo.RareCatVO;
import com.github.computer114514.domain.vo.SecKillVO;
import com.github.computer114514.mapper.RareCatMapper;
import com.github.computer114514.service.BuyRareLogService;
import com.github.computer114514.service.CatService;
import com.github.computer114514.service.RareCatService;
import com.github.computer114514.utils.RedisIdWorker;
import com.github.computer114514.utils.UserContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RareCatServiceImpl extends ServiceImpl<RareCatMapper,RareCat> implements RareCatService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RareCatMapper rareCatMapper;
    @Autowired
    private BuyRareLogService buyRareLogService;
    @Autowired
    private CatService catService;
    @Autowired
    private RedisIdWorker redisIdWorker;
    @Autowired
    private RedissonClient redissonClient;

    private static final DefaultRedisScript<Long> RARE_CAT_SECKILL_SCRIPT;

    static{
        RARE_CAT_SECKILL_SCRIPT= new DefaultRedisScript<>();
        //定义一个script对象。
        RARE_CAT_SECKILL_SCRIPT.setLocation(new ClassPathResource("RareCatSecKill.lua"));
        //设置lua位置
        RARE_CAT_SECKILL_SCRIPT.setResultType(Long.class);
        //设置返回类型
    }

    /**
     *得到珍惜猫图片
     * @return
     */
    @Override
    public RareCatVO getRareCat() {
        RareCat currentRareCat = getCurrentRareCat();
        if(currentRareCat==null){
            return null;
        }
            return BeanUtil.copyProperties(currentRareCat, RareCatVO.class);
        //查到的数据拷贝进去,返回
    }

    /**
     * 得到珍惜猫秒杀时间。
     * @return
     */
    @Override
    public Result<SecKillVO> getSecKillTime() {
        RareCat currentRareCat = getCurrentRareCat();
        //获取三段时间
        LocalDateTime beginTime = currentRareCat.getBeginTime();
        DateTime date = DateUtil.date(beginTime);
        long BeginTimeStamp = date.getTime();
        log.debug("什锦锉",BeginTimeStamp);


        long EndTimeStamp = DateUtil.date(currentRareCat.getEndTime()).getTime();
        log.debug("什锦锉",EndTimeStamp);

        LocalDateTime now = LocalDateTime.now();
        long NowTimeStamp = DateUtil.date(now).getTime();
        log.debug("什锦锉",NowTimeStamp);

        //返回数据判断。
        SecKillVO secKillVO = new SecKillVO();

        if(NowTimeStamp<BeginTimeStamp){
            secKillVO.setRemainTime((int) ((BeginTimeStamp - NowTimeStamp) / 1000));
        }
        else if(NowTimeStamp<EndTimeStamp){
            secKillVO.setRemainTime(0);
        }else{
            secKillVO.setRemainTime(-1);
        }
        return Result.success(secKillVO);
    }
    /**
     * 下单,买珍惜猫
     * @return
     */
    @Override
    @Transactional
    public Result<Void> buyRareCat(String rareCatId) throws InterruptedException {
        int userId = UserContext.getUser().getId();

        Long StockAndOneCheck = stringRedisTemplate.execute(RARE_CAT_SECKILL_SCRIPT
                , Collections.emptyList()
                , rareCatId
                , Integer.toString(userId));

        if(StockAndOneCheck!=0){
            return Result.error(StockAndOneCheck==1?"库存不足":"一人多单可不行哦");
        }

        LocalDateTime now = LocalDateTime.now();
        //获取操作用户id

        //检查库存。
        RareCat currentRareCat = this.getCurrentRareCat();


        //观察仓库里有没有这个用户的这个猫，如果有，返回错误信息。
        String catId = currentRareCat.getId();
        //构造有无条件

//        LambdaQueryWrapper<Cat> catLambdaQueryWrapper =
//                new LambdaQueryWrapper<Cat>()
//                        .eq(Cat::getCatId,catId)
//                        .eq(Cat::getUserId,userId);
//        Cat one = catService.getOne(catLambdaQueryWrapper);
//        if(one!=null){
//            return Result.error("你已经买了");
//        }

        //将猫存入当前用户的仓库。扣减库存，同时判断getCurrentRareCat是否超时。;
//        LambdaUpdateWrapper<RareCat> BuyRareCatWrapper = new LambdaUpdateWrapper<>();
//        LambdaUpdateWrapper<RareCat> wrapper = BuyRareCatWrapper
//                .setSql("store = store-1")
//                .lt(RareCat::getBeginTime, now)
//                .gt(RareCat::getEndTime, now);
//        RareCat::getBeginTime等价于return RareCat.getBeginTime()
//        RareCat currentRareCat = getOne(wrapper);
        boolean success = update().setSql("store = store-1")
                .gt("store", 0).update();
            if(!success){
                return Result.error("卖光了");
            }

        //检查时间正确否。
        DateTime date = DateUtil.date(currentRareCat.getEndTime());
        long EndTimeStamp = date.getTime();

        long NowTimeStamp = DateUtil.date(now).getTime();
        if(NowTimeStamp>EndTimeStamp){
            return Result.error("时间过了");
        }
        //猫、猫
        Cat cat = new Cat();
        cat.setCatId(catId);
        cat.setUserId((long) userId);
        cat.setName(currentRareCat.getName());
        cat.setImageUrl(currentRareCat.getUrl());
        //保存猫到当前用户的仓库。

        //这里获取锁，因为这里是事故，保证一个一个来。

        //获取对象，一个用户一把锁，防止一人多单。
        RLock lock = redissonClient.getLock("lock:seckill:user"+userId);
        boolean isLock = lock.tryLock(10, 120, TimeUnit.SECONDS);
        if(!isLock){
            return Result.error("服务器繁忙");
        }

        //线程们只能单个执行的逻辑
        try {
            boolean save = catService.save(cat);
            if(!save){
                return Result.error("错误!保存珍惜猫失败!");
            }
            //记录当前用户操作日志
            BuyRareLog log = new BuyRareLog();
            log.setId(redisIdWorker.nextId("BuyRareLog"));
            log.setDes("买入id为"+catId+"的珍惜猫");
            log.setUserId(userId);

            boolean saved = buyRareLogService.save(log);
            if(!saved){
                return Result.error("错误!保存操作日志失败!");
            }

            return Result.success();

        } catch (Exception e) {
            return Result.error("未知错误,抢购失败");
        }finally {
            //总要解锁
            lock.unlock();
        }
    }

    /**
     * 得到现在的珍惜猫。
     * @return
     */
    private RareCat getCurrentRareCat() {
        //构造条件where begin_time<now and end_time>now
        LambdaQueryWrapper<RareCat> CorrectTimeWrapper = new LambdaQueryWrapper<>();
        LocalDateTime now = LocalDateTime.now();
//        RareCat::getBeginTime等价于return RareCat.getBeginTime()
//        CorrectTimeWrapper.lt(RareCat::getBeginTime,now);
//        CorrectTimeWrapper.gt(RareCat::getEndTime,now);
        //筛选
        return getOne(CorrectTimeWrapper);
    }

}
