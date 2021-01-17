package com.dudu.huoyun.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    //id主键
    private Long id;
    //司机姓名
    private String name;
    //司机生日
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birth;
    //户籍地址
    private String registryAddress;
    //驾龄
    private String drivingExp;
    //身份证号码
    private String idCardNumber;
    //电话号码
    private String phone;
    //紧急联系人
    private String emergencyMan;
    //紧急联系人电话号码
    private String emergencyPhone;
    //车牌号
    private String vehicleNumber;
    //车辆类型
    private String vehicleType;
    //车辆类型值
    private String vehicleTypeName;
    //马力
    private String vehicleHp;
    //轮胎
    private String vehicleTyre;
    //车架号
    private String vehicleFrameNum;
    //发动机号
    private String vehicleEngineNum;
    //保险到期日
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date insuranceDate;
    //车辆注册日
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date registerDate;
    //正式运营时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date operateDate;
    //驾驶证照片
    private String driverLicense;
    //行车证照片
    private String vehicleLicense;
    //gps设备号
    private String gpsNum;
    //纬度
    private String lat;
    //经度
    private String lng;
    //加盟类型
    private String joinType;
    //创建时间

    private Date createTime;
    //更新时间

    private Date updateTime;
    //所属公司
    private Long belongCompanyId;

    //备注
    private String remark;
    /**
     * 年龄
     *
     * @return
     */
    private String age;

}
