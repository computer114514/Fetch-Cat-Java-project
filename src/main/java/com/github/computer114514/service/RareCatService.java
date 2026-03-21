package com.github.computer114514.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.computer114514.domain.enity.RareCat;
import com.github.computer114514.domain.enity.Result;
import com.github.computer114514.domain.vo.RareCatVO;
import com.github.computer114514.domain.vo.SecKillVO;

public interface RareCatService extends IService<RareCat> {

    RareCatVO getRareCat();

    Result<SecKillVO> getSecKillTime();
}
