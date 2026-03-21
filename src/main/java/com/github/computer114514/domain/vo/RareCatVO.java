package com.github.computer114514.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RareCatVO {
        private  String id;
        private  String name;
        private  String url;
        private Integer store;
        private LocalDateTime beginTime;
        private LocalDateTime endTime;
}
