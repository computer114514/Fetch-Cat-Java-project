package com.github.computer114514.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.computer114514.mapper.CatMapper;
import com.github.computer114514.domain.enity.Cat;
import com.github.computer114514.service.CatService;
import com.github.computer114514.utils.CacheClient;
import com.github.computer114514.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.computer114514.constant.CatConstant.CACHE_CAT_KEY;

@Service
public class CatServiceImpl extends ServiceImpl<CatMapper,Cat> implements CatService {
    @Autowired
    private CatMapper catMapper;
     @Autowired
    private CacheClient cacheClient;
    @Override
    public void saveCat(String url, String id, String name) {
        //url是什么？是图片链接，需要处理封装
        Long userId = (long) UserContext.getUser().getId();
        Cat cat = new Cat(null, userId, id, name, url, null, null, null, null, null, null);
//        catMapper.InsertCat(url, id, name,userId);
        this.save(cat);
    }

    @Override
    public List<Cat> getCat() {
        List<Cat> CatList= searchCatsList(getUserIdInThreadLocal());

        return CatList;
    }

    @Override
    public void delCat(String catId) {
        delCat(catId,getUserIdInThreadLocal());
    }

    @Override
    public void updateCat(Cat cat) {
        updateCat(cat,getUserIdInThreadLocal());
    }

    @Override
    public Cat getCatById(String catId) {
/**
 * 穿透
 */
//        return cacheClient.queryWithPassThrough(CACHE_CAT_KEY,catId,Cat.class,this::getCatByIdWrapper,30L,
//                TimeUnit.MINUTES);

      return cacheClient.queryWithLogicalExpire(CACHE_CAT_KEY,catId,
              Cat.class,this::getCatByIdWrapper,30L,
              TimeUnit.MINUTES);


//        return getCatById(catId,getUserIdInThreadLocal());

    }
@Override
    public List<Cat> searchCatsList(Long userId) {
        // 1. 创建 Wrapper (Lambda 写法最安全，不会写错字段名)
        LambdaQueryWrapper<Cat> wrapper = new LambdaQueryWrapper<>();

        // 2. 构造条件: cat 表中的 userId 字段 等于 传入的 userId
        // Cat::getUserId 会自动识别为数据库的 user_id 列
        wrapper.eq(Cat::getUserId, userId);

        // 3. 执行查询 (BaseMapper 自带的方法)
        return this.baseMapper.selectList(wrapper);
    }
    @Override
    public void delCat(String  catId,Long userId) {
        // 1. 创建 Wrapper (Lambda 写法最安全，不会写错字段名)
        LambdaQueryWrapper<Cat> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Cat::getUserId, userId).eq(Cat::getCatId,catId);

        // 3. 执行查询 (BaseMapper 自带的方法)
        this.remove(wrapper);
    }
    @Override
    public void updateCat(Cat cat, Long userId) {
        LambdaUpdateWrapper<Cat> wrapper = new LambdaUpdateWrapper<>();

        // 条件
        wrapper.eq(Cat::getCatId, cat.getCatId())
                .eq(Cat::getUserId, userId);

        // 普通赋值
        wrapper.set(Cat::getName, cat.getName());

        // 自增赋值 (使用 MP 的 SFunction 和 值)
        // 语法：wrapper.set(实体类::字段, "字段名 + 值") -> 这种不行
        // 正确的高级写法是利用 SqlInjector，但对于新手，推荐直接用 setSql 并格式化
        // 为了防止 SQL 注入，最好用占位符，但 setSql 对占位符支持有限。
        // 既然 favorability 是整数，直接拼接是安全的。

        wrapper.setSql("favorability = favorability + " + (cat.getFavorability() != null ? cat.getFavorability() : 0));
        wrapper.setSql("pet_count = pet_count + " + (cat.getPetCount() != null ? cat.getPetCount() : 0));
        wrapper.setSql("walk_count = walk_count + " + (cat.getWalkCount() != null ? cat.getWalkCount() : 0));

        this.baseMapper.update(null, wrapper);
    }
    @Override
    public Cat getCatByIdWrapper(String  catId) {

        int userId = UserContext.getUser().getId();
        // 1. 创建 Wrapper (Lambda 写法最安全，不会写错字段名)
        LambdaQueryWrapper<Cat> wrapper = new LambdaQueryWrapper<>();
        // 2. 构造条件: cat 表中的 userId 字段 等于 传入的 userId
        // Cat::getUserId 会自动识别为数据库的 user_id 列
        wrapper.eq(Cat::getUserId, userId).eq(Cat::getCatId,catId);
        // 3. 执行查询 (BaseMapper 自带的方法)
        return this.baseMapper.selectOne(wrapper);
    }
    public Long getUserIdInThreadLocal(){
        return (long) UserContext.getUser().getId();
    }


}
