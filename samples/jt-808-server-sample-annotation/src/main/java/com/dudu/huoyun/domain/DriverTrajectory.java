package com.dudu.huoyun.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhl
 * @email 1992lcg@163.com
 * @date 2020-12-10 15:14:44
 */
@Data
@Builder
public class DriverTrajectory implements Serializable {
    private static final long serialVersionUID = 1L;

    //id主键
    private Long id;
    //客户id
    private Long driverId;
    //车牌号
    private String plateNumber;
    //gps设备号
    private String gpsId;
    //纬度
    private String lat;
    //经度
    private String lon;
    //创建时间
    private Date createTime;


}
