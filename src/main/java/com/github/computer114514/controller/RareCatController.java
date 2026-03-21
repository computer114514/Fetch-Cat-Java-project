package com.github.computer114514.controller;

import com.github.computer114514.domain.enity.Result;
import com.github.computer114514.domain.vo.RareCatVO;
import com.github.computer114514.domain.vo.SecKillVO;
import com.github.computer114514.service.RareCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rareCat")
public class RareCatController {
    @Autowired
    private RareCatService rareCatService;
    @GetMapping("/getRareCat")
    public Result<RareCatVO> getRareCat(){
        RareCatVO res =rareCatService.getRareCat();
        return Result.success(res);
    }
    @GetMapping("/getRemainTime")
    public Result<SecKillVO> getRemainTime(){
       return rareCatService.getSecKillTime();
    }
}
