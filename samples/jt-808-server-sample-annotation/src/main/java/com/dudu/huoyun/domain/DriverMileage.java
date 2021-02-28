package com.dudu.huoyun.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverMileage {
    private static final long serialVersionUID = 1L;
    //id主键
    private Long id;
    //司机id
    private Long driverId;
    //日期
    private Date mDate;
    //里程，单位 米
    private Double mileage;
    //运单里程，单位 米
    private Double watbilMileage;
    //创建时间
    private Date createTime;
}
