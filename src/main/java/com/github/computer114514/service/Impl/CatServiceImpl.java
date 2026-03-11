package com.github.computer114514.service.Impl;

import com.github.computer114514.mapper.CatMapper;
import com.github.computer114514.pojo.Cat;
import com.github.computer114514.service.CatService;
import com.github.computer114514.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatServiceImpl implements CatService {
    @Autowired
    CatMapper catMapper;
    @Override
    public void saveCat(String url, String id, String name) {
        //url是什么？是图片链接，需要处理封装
        Long userId = UserContext.getId();
        catMapper.InsertCat(url, id, name,userId);
    }

    @Override
    public List<Cat> getCat() {
        Long userId = UserContext.getId();
        List<Cat> CatList= catMapper.searchCatsList(userId);
        return CatList;
    }

    @Override
    public void delCat(String catId) {
        Long userId = UserContext.getId();
        catMapper.delCat(catId,userId);
    }

    @Override
    public void updateCat(Cat cat) {
        Long userId = UserContext.getId();
        catMapper.updateCat(cat,userId);
    }

    @Override
    public Cat getCatById(String catId) {
        Long userId = UserContext.getId();
        return catMapper.getCatById(catId,userId);
    }
}
