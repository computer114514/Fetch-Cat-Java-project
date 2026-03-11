package com.github.computer114514.controller;

import com.github.computer114514.pojo.Cat;
import com.github.computer114514.pojo.Result;
import com.github.computer114514.service.Impl.CatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/cat")
public class CatController {
    @Autowired
    CatServiceImpl catService;

    @PostMapping("/save")
    public Result saveCat(@RequestBody Cat cat){
        catService.saveCat(cat.getImageUrl(),cat.getCatId(),cat.getName());
        return Result.success();
    }

    @GetMapping("/get")
    public Result getCat(){
        List<Cat> list=catService.getCat();
        return Result.success(list);
    }
    @DeleteMapping("/del/{catId}")
    public Result delCat(@PathVariable String catId){
        catService.delCat(catId);
        return Result.success();
    }
    @PutMapping("/update")
    public Result updateCat(@RequestBody Cat cat){
        catService.updateCat(cat);
        return Result.success();
    }
    @GetMapping("/get/{catId}")
    public Result getCatById(@PathVariable String catId){
        Cat cat=catService.getCatById(catId);
        return Result.success(cat);
    }
    }
