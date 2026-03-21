package com.github.computer114514.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.computer114514.domain.enity.RareCat;
import com.github.computer114514.domain.enity.Result;
import com.github.computer114514.domain.vo.RareCatVO;
import com.github.computer114514.domain.vo.SecKillVO;
import com.github.computer114514.mapper.RareCatMapper;
import com.github.computer114514.service.RareCatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RareCatServiceImpl extends ServiceImpl<RareCatMapper,RareCat> implements RareCatService {
    @Autowired
    private RareCatMapper rareCatMapper;

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
