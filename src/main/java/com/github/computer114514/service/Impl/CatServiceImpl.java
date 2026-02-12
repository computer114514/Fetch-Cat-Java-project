package com.github.computer114514.service.Impl;

import com.github.computer114514.mapper.CatMapper;
import com.github.computer114514.pojo.Cat;
import com.github.computer114514.service.CatService;
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
        catMapper.InsertCat(url, id, name);
    }

    @Override
    public List<Cat> getCat() {
        List<Cat> CatList= catMapper.searchCatsList();
        return CatList;
    }

    @Override
    public void delCat(String catId) {
        catMapper.delCat(catId);
    }
}
