package com.dudu.huoyun.service;

import com.alibaba.fastjson.JSONObject;
import com.dudu.huoyun.domain.Driver;
import com.dudu.huoyun.domain.DriverTrajectory;
import com.dudu.idb.service.CmmDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
// CHECKSTYLE:OFF
@Slf4j
@Service
public class DuduService {
    @Autowired
    CmmDataService cmmDataService;

    public int saveGPS(String gpsNum, Double lng, Double lat, String time) {
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

        //插入轨迹
        DriverTrajectory trace = DriverTrajectory.builder()
                .driverId(driverId)
                .plateNumber(driver.getString("vehicle_number"))
                .gpsId(gpsNum)
                .lat(_lat)
                .lon(_lng)
                .createTime(transTime(time)).build();
        int cnt = cmmDataService.insert("gps_trace_save", trace);
        return cnt;

    }

    private Date transTime(String time) {
        try {
            return DateUtils.parseDate(time, "yyMMddHHmmss");
        } catch (Exception e) {
            return new Date();
        }
    }

}
