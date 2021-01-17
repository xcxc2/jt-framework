package com.dudu.idb.service;

import com.dudu.idb.util.Pagination;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface CmmDataService {
    /**
     * 基础查询方法【列表】
     */
    public List findList(String dataType, Object paramMap);

    /**
     * 基础查询方法【单个对象（或Map）】
     */
    public Object findOne(String dataType, Object paramMap);

    /**
     * 通用数据查询接口
     *
     * @param dataType - 数据查询类型[ home_xxx ，约定以 组名_sqlID 为名]
     * @param paramMap - 查询参数
     * @return
     */
    public List<Map> findDataList(String dataType, Map<String, Object> paramMap);

    //通用数据查询接口：返回第一条数据的，单个对象的Map结构
    public Map findDataOne(String dataType, Map<String, Object> paramMap);

    //扩展接口：返回列表，列表只包含一列信息
    public List findDataListOne(String dataType, Map<String, Object> paramMap);

    //扩展接口：返回Map列表，key -id  ，value -name
    public Map findDataListMapId(String dataType, Map<String, Object> paramMap);

    //扩展接口：返回Map列表，key -name  ，value -id
    public Map findDataListMap(String dataType, Map<String, Object> paramMap);

    /**
     * 通用分页数据查询接口
     *
     * @param pagination -分页类
     * @param dataType   - 数据查询类型[ home_xxx ，约定以 组名_sqlID 为名]
     * @param paramMap   - 查询参数
     * @return
     */
    public PageInfo findDataPage(Pagination pagination, String dataType, Map<String, Object> paramMap);

    public int insert(String dataType, Object paramMap);

    public int update(String dataType, Object paramMap);

    public int delete(String dataType, Object paramMap);

    //批量更新
    int batch(String dataType, List paramList);

    int batch(String dataType, List paramList, int batchSize);
}
