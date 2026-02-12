package com.github.computer114514.service;


import com.github.computer114514.mapper.CatMapper;
import com.github.computer114514.pojo.Cat;

import java.util.List;

public interface CatService {
    public void saveCat(String url, String id, String name);

    public List<Cat> getCat();

    void delCat(String catId);
}
