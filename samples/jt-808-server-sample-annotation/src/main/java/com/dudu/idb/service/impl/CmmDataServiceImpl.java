package com.dudu.idb.service.impl;

import com.dudu.idb.service.CmmDataService;
import com.dudu.idb.util.Pagination;
import com.dudu.idb.util.ToolsUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// CHECKSTYLE:OFF
@Service
public class CmmDataServiceImpl implements CmmDataService {
    @Value("${idb.pkg:com.dudu.idb}")
    private String CMM_PKG = "com.dudu.idb";

    private final static Logger logger = LoggerFactory.getLogger(CmmDataServiceImpl.class);
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private String getSqlId(String dataType) {
        //切换数据库
//        switchDS(dataType);
        logger.info("开始执行[dataType=" + dataType + "]...");
        dataType = StringUtils.replace(dataType, "_", ".", 1);//home_userCnt => home.userCnt
        return CMM_PKG + "." + dataType;
    }

    /**
     * 基础查询方法【列表】
     */
    @Override
    public List findList(String dataType, Object paramMap) {
        if (paramMap == null) paramMap = new HashMap<>();
        String sqlId = getSqlId(dataType);
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        List<Map> list = null;
        try {
            list = sqlSession.selectList(sqlId, paramMap);
        } catch (Exception e) {
            String error = "查询[dataType=" + dataType + "]出错：" + e.getMessage();
            logger.error(error, e);
            throw new RuntimeException(error, e);
//            return Collections.singletonList((Map)Collections.singletonMap("error", error));
        } finally {
            closeSqlSession(sqlSession, dataType);
        }
        return list;
    }

    /**
     * 基础查询方法【单个对象（或Map）】
     */
    @Override
    public Object findOne(String dataType, Object paramMap) {
        List list = findList(dataType, paramMap);
        if (list.size() > 0) {
            return transObject(list.get(0), paramMap);
        }
        return null;
    }

    private Object transObject(Object object, Object paramMap) {//判断如果是Map对象，进行结果转换
        if (object instanceof Map && paramMap instanceof Map) {
            String keyType = (String) ((Map) paramMap).get("keyType");//是否驼峰转换，keyType=hump/lower
            if ("hump".equals(keyType)) {//转驼峰
                return ToolsUtil.mapToHumpKey(((Map) object));
            } else {//默认转小写
                return ToolsUtil.mapToLowerKey(((Map) object));
            }
        }
        return object;
    }

    @Override
    public List<Map> findDataList(String dataType, Map<String, Object> paramMap) {
        if (paramMap == null) paramMap = new HashMap<>();
        List<Map> list = findList(dataType, paramMap);
        String keyType = (String) paramMap.get("keyType");//是否驼峰转换，keyType=hump/lower
        if ("hump".equals(keyType)) {//转驼峰
            return ToolsUtil.mapToHumpKey(list);
        } else {//默认转小写
            return ToolsUtil.mapToLowerKey(list);
        }
    }

    @Override
    public Map findDataOne(String dataType, Map<String, Object> paramMap) {
        List<Map> maps = findDataList(dataType, paramMap);
        if (maps.size() > 0) return maps.get(0);
        return new HashMap();
    }

    //返回列表，列表只包含第一列信息
    @Override
    public List findDataListOne(String dataType, Map<String, Object> paramMap) {
        List<Map> maps = findDataList(dataType, paramMap);
        List<Object> list = new ArrayList<Object>();
        for (Map map : maps) {
            list.add(map.values().iterator().next());
        }
        return list;
    }

    //返回Map列表，key -id  ，value -name
    @Override
    public Map findDataListMapId(String dataType, Map<String, Object> paramMap) {
        List<Map> maps = findDataList(dataType, paramMap);
        Map result = new HashMap();
        for (Map map : maps) {
            result.put(String.valueOf(map.get("id")), map.get("name"));
        }
        return result;
    }

    //返回Map列表，key -name  ，value -id
    @Override
    public Map findDataListMap(String dataType, Map<String, Object> paramMap) {
        List<Map> maps = findDataList(dataType, paramMap);
        Map result = new HashMap();
        for (Map map : maps) {
            result.put(String.valueOf(map.get("name")), map.get("id"));
        }
        return result;

    }


    @Override
    public PageInfo findDataPage(Pagination pagination, String dataType, Map<String, Object> paramMap) {
        if (paramMap == null) paramMap = new HashMap<>();
        String sqlId = getSqlId(dataType);
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        try {
            PageHelper.startPage(pagination.getCurrentPage(), pagination.getPageSize());
            List<Map> list = sqlSession.selectList(sqlId, paramMap);
            //查询后，必须先用PageInfo对结果进行包装，不然总数查不到
            PageInfo page = new PageInfo(list);

            String keyType = (String) paramMap.get("keyType");//是否驼峰转换，keyType=hump/lower
            if ("hump".equals(keyType)) {//转驼峰
                page.setList(ToolsUtil.mapToHumpKey(page.getList()));
            } else {//默认转小写
                page.setList(ToolsUtil.mapToLowerKey(page.getList()));
            }
            return page;
        } catch (Exception e) {
            String error = "分页查询[dataType=" + dataType + "]出错：" + e.getMessage();
            throw new RuntimeException(error, e);
        } finally {
            closeSqlSession(sqlSession, dataType);
        }
    }

    @Override
    public int insert(String dataType, Object paramMap) {
        if (paramMap == null) paramMap = new HashMap<>();
        String sqlId = getSqlId(dataType);
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        try {
            return sqlSession.insert(sqlId, paramMap);
        } catch (Exception e) {
            String error = "插入[dataType=" + dataType + "]出错：" + e.getMessage();
            throw new RuntimeException(error, e);
        } finally {
            closeSqlSession(sqlSession, dataType);
        }
    }

    @Override
    public int update(String dataType, Object paramMap) {
        if (paramMap == null) paramMap = new HashMap<>();
        String sqlId = getSqlId(dataType);
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        try {
            return sqlSession.update(sqlId, paramMap);
        } catch (Exception e) {
            String error = "更新[dataType=" + dataType + "]出错：" + e.getMessage();
            throw new RuntimeException(error, e);
        } finally {
            closeSqlSession(sqlSession, dataType);
        }
    }


    @Override
    public int delete(String dataType, Object paramMap) {
        if (paramMap == null) paramMap = new HashMap<>();
        String sqlId = getSqlId(dataType);
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        try {
            return sqlSession.delete(sqlId, paramMap);
        } catch (Exception e) {
            String error = "删除[dataType=" + dataType + "]出错：" + e.getMessage();
            throw new RuntimeException(error, e);
        } finally {
            closeSqlSession(sqlSession, dataType);
        }
    }

    @Override
    public int batch(String dataType, List paramList) {
        return batch(dataType, paramList, 1000);
    }

    @Override
    public int batch(String dataType, List paramList, int bacheSize) {
        String sqlId = getSqlId(dataType);
        SqlSession sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory, ExecutorType.BATCH, null);
        List<List<Object>> subLists = ListUtils.partition(paramList, bacheSize);
        int result = 0;
        try {
            for (List subList : subLists) {
                for (Object obj : subList) {
                    result += sqlSession.update(sqlId, obj);
                }
                sqlSession.commit();
            }
            return result;
        } catch (Exception e) {
            sqlSession.rollback();
            String error = "更新[dataType=" + dataType + "]出错：" + e.getMessage();
            throw new RuntimeException(error, e);
        } finally {
            closeSqlSession(sqlSession, dataType);
        }
    }

    private void closeSqlSession(SqlSession sqlSession, String dataType) {
        SqlSessionUtils.closeSqlSession(sqlSession, sqlSessionFactory);
//        clearDS(dataType);
    }


}
