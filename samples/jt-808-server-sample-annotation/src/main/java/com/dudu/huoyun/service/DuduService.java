package com.dudu.huoyun.service;

import com.alibaba.fastjson.JSONObject;
import com.dudu.huoyun.domain.Driver;
import com.dudu.huoyun.domain.DriverMileage;
import com.dudu.huoyun.domain.DriverTrajectory;
import com.dudu.huoyun.utils.GeoUtils;
import com.dudu.idb.service.CmmDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
// CHECKSTYLE:OFF
@Slf4j
@Service
public class DuduService {
    @Autowired
    CmmDataService cmmDataService;

    public int saveGPS(String gpsNum, Double lng, Double lat, String time) {
        if(lng==0D)return 0;
        String _lng = lng.toString();
        String _lat = lat.toString();
        //driver_id,d.vehicle_number
        Driver query = Driver.builder().gpsNum(gpsNum).build();
        JSONObject driver = (JSONObject) cmmDataService.findOne("gps_driver_find", query);
        if (driver == null) return 0;
        Long driverId = driver.getLong("id");

        //更新 实时位置
        Driver driverUp = Driver.builder().gpsNum(gpsNum).lng(_lng).lat(_lat).build();
        cmmDataService.update("gps_driver_update", driverUp);

        //查询上一轨迹点，计算就算距离
        DriverTrajectory lastTrace = findLastTrace(gpsNum);
        double mileage = 0;
        if(lastTrace!=null ){
            double lng1 = Double.valueOf(lastTrace.getLon());
            double lat1 = Double.valueOf(lastTrace.getLat());
            mileage = GeoUtils.getDistance(lng1,lat1,lng,lat);
        }
        //插入轨迹
        DriverTrajectory trace = DriverTrajectory.builder()
                .driverId(driverId)
                .plateNumber(driver.getString("vehicle_number"))
                .gpsId(gpsNum)
                .lat(_lat)
                .lon(_lng)
                .mileage(mileage)
                .createTime(transTime(time)).build();
        int cnt = cmmDataService.insert("gps_trace_save", trace);
        //更新每日司机里程表
        Date curDate = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        DriverMileage driverMileage =  findDriverMileage(driverId,curDate);
        if(driverMileage!=null){
            driverMileage.setMileage(driverMileage.getMileage()+mileage);
            cmmDataService.update("mileage_update",driverMileage);
        }else {
            driverMileage=DriverMileage.builder()
                    .driverId(driverId).mDate(curDate).mileage(mileage).build();
            cmmDataService.insert("mileage_save",driverMileage);
        }


        return cnt;

    }

    /**
     * 查找上一轨迹点
     * @param gpsNum
     * @return
     */
    public DriverTrajectory findLastTrace(String gpsNum){
        DriverTrajectory query2 = DriverTrajectory.builder().gpsId(gpsNum).build();
        return (DriverTrajectory) cmmDataService.findOne("trace_find", query2);
    }

    public DriverMileage findDriverMileage(Long driverId, Date mDate){
        DriverMileage query2 = DriverMileage.builder()
                .driverId(driverId)
                .mDate(mDate).build();
        return (DriverMileage) cmmDataService.findOne("mileage_find", query2);
    }

    private Date transTime(String time) {
        try {
            return DateUtils.parseDate(time, "yyMMddHHmmss");
        } catch (Exception e) {
            return new Date();
        }
    }

}
