package com.github.computer114514.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.computer114514.domain.enity.Cat;
import com.github.computer114514.mapper.CatMapper;

import java.util.List;

public interface CatService extends IService<Cat> {
    public void saveCat(String url, String id, String name);

    public List<Cat> getCat();

    void delCat(String catId);

    void updateCat(Cat cat);

    Cat getCatById(String catId);

    public List<Cat> searchCatsList(Long userId);

    public void delCat(String  catId,Long userId);

    public void updateCat(Cat cat, Long userId);

    public Cat getCatByIdWrapper(String  catId);
}
