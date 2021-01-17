package com.dudu.idb.controller;

import com.dudu.idb.service.CmmDataService;
import com.dudu.idb.util.Pagination;
import com.dudu.idb.util.ToolsUtil;
import com.github.pagehelper.PageInfo;
import io.github.hylexus.jt808.samples.annotation.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// CHECKSTYLE:OFF
/**
 * 通用数据查询接口
 */
@RestController
@RequestMapping("/cmm")
public class CmmDataController {

    private static final Logger logger = LoggerFactory.getLogger(CmmDataController.class);

    @Autowired
    private CmmDataService cmmDataService;

    /**
     * 1. 通用数据查询-单个对象
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/one", method = RequestMethod.GET)
    public Result findOne(@RequestParam String dataType, @RequestParam HashMap<String, Object> params) {
        logger.debug("通用数据查询-单个对象one[dataType=" + dataType + "],参数：" + params);
        try {
            List list = cmmDataService.findDataList(dataType, params);
            if (list.size() > 0) {
                return Result.doneData(list.get(0));
            } else {
                return Result.doneData(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error("dataType=" + dataType + " 查询失败", e);
            return Result.failData(e.getMessage());
        }
    }

    @RequestMapping(value = "/one", method = RequestMethod.POST)
    public Result findOnePost(@RequestBody HashMap<String, Object> params) {
        String dataType = (String) params.get("dataType");
        return findOne(dataType, params);
    }

    /**
     * 2. 通用数据查询-listMap，多个对象里列表格式
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result findListMap(@RequestParam String dataType, @RequestParam HashMap<String, Object> params) {
        logger.debug("通用数据查询-列表对象listMap[dataType=" + dataType + "],参数：" + params);
        try {
            List list = cmmDataService.findDataList(dataType, params);
            return Result.doneData(list);
        } catch (Exception e) {
            return Result.failData(e.getMessage());
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result findListMapPost(@RequestBody HashMap<String, Object> params) {
        String dataType = (String) params.get("dataType");
        return findListMap(dataType, params);
    }

    /**
     * 3.通用数据查询-mapList，多个对象属性分别合并到属性数组中
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/array", method = RequestMethod.GET)
    public Result findArray(@RequestParam String dataType, @RequestParam HashMap<String, Object> params) {
        logger.debug("通用数据查询-数组列表MapList[dataType=" + dataType + "],参数：" + params);
        try {
            List<Map> list = cmmDataService.findDataList(dataType, params);
            // 转成表头，数据行
            return Result.doneData(ToolsUtil.listToMap(list));
        } catch (Exception e) {
            return Result.failData(e.getMessage());
        }
    }

    @RequestMapping(value = "/array", method = RequestMethod.POST)
    public Result findArrayPost(@RequestBody HashMap<String, Object> params) {
        String dataType = (String) params.get("dataType");
        return findArray(dataType, params);
    }

    /**
     * 4.通用数据查询-统一接口 union，
     *
     * @return
     */
    @RequestMapping(value = "/union", method = RequestMethod.POST)
    public Result findUnion(@RequestBody Map<String, Object> paramMap) {
        logger.debug("通用数据查询-统一查询union:" + paramMap);
        String dataType = (String) paramMap.get("dataType");
        String viewType = (String) paramMap.get("viewType");
        Object result;
        try {
            List<Map> list = cmmDataService.findDataList(dataType, paramMap);
            if ("one".equalsIgnoreCase(viewType)) {//单个对象
                if (list.size() > 0) {
                    result = list.get(0);
                } else {
                    result = new HashMap<>();
                }
            } else if ("array".equalsIgnoreCase(viewType)) {//转成数组
                result = ToolsUtil.listToMap(list);
            } else {//默认 按 list 走
                result = list;
            }
            return Result.doneData(result);
        } catch (Exception e) {
            return Result.failData(e.getMessage());
        }
    }

    /**
     * 通用数据查询-map列表
     *
     * @return
     */
    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public Result findGroup(@RequestBody List<Map<String, Object>> dataTypeList) {
        logger.debug("通用数据查询-聚合查询group:" + dataTypeList);
        Map<String, Object> result = new HashMap<>();
        for (Map<String, Object> paramMap : dataTypeList) {
            String dataType = (String) paramMap.get("dataType");
            String dataTypeRet = dataType;//返回类型
            String dataTypeIdx = (String) paramMap.get("dataTypeIdx");//类型序号，防止同一dataType，调用多次请求
            if (StringUtils.isNotBlank(dataTypeIdx)) {
                dataTypeRet += dataTypeIdx;
            }
            String viewType = (String) paramMap.get("viewType");
            try {
                List<Map> list = cmmDataService.findDataList(dataType, paramMap);
                if ("one".equalsIgnoreCase(viewType)) {//单个对象
                    if (list.size() > 0) {
                        result.put(dataTypeRet, list.get(0));
                    } else {
                        result.put(dataTypeRet, new HashMap<>());
                    }
                } else if ("array".equalsIgnoreCase(viewType)) {//转成数组
                    result.put(dataTypeRet, ToolsUtil.listToMap(list));
                } else {//默认 按 list 走
                    result.put(dataTypeRet, list);
                }
            } catch (Exception e) {
                result.put(dataType, e.getMessage());
            }
        }
        return Result.doneData(result);

    }


    /**
     * 1. 通用数据查询-单个对象
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Result update(@RequestParam String dataType, @RequestParam HashMap<String, Object> params) {
        String action = (String) params.get("action");
        logger.debug("通用数据执行-[dataType=" + dataType + ",action=" + action + "],参数：" + params);
        try {
            int result = -1;
            if ("insert".equals(action)) {
                result = cmmDataService.insert(dataType, params);
            } else if ("update".equals(action)) {
                result = cmmDataService.update(dataType, params);
            } else if ("delete".equals(action)) {
                result = cmmDataService.delete(dataType, params);
            } else {//默认 update
                result = cmmDataService.update(dataType, params);
            }
            return Result.doneData(result);
        } catch (Exception e) {
            logger.error("dataType=" + dataType + " 执行失败", e);
            return Result.failData(e.getMessage());
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result updatePost(@RequestBody HashMap<String, Object> params) {
        String dataType = (String) params.get("dataType");
        return update(dataType, params);
    }

    @GetMapping(value = "/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam Map<String, Object> paramMap) {
        String dataType = (String) paramMap.get("dataType");
        String keyType = (String) paramMap.get("keyType");//是否驼峰转换，keyType=hump/lower
        logger.debug("通用数据分页查询-[dataType=" + dataType + ",keyType=" + keyType + "],参数：" + paramMap);

        Pagination pagination = Pagination.transform(pageNum, pageSize);
        PageInfo pageInfo = cmmDataService.findDataPage(pagination, dataType, paramMap);


        return Result.doneData(pageInfo);
    }

    @PostMapping(value = "/page")
    public Result findPagePost(@RequestBody Map<String, Object> paramMap) {
        Integer pageNum = ToolsUtil.toInteger(paramMap.get("pageNum"));
        Integer pageSize = ToolsUtil.toInteger(paramMap.get("pageSize"));
        return findPage(pageNum, pageSize, paramMap);

    }


}
