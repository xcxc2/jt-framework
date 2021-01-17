package com.dudu.idb.util;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// CHECKSTYLE:OFF
public class ToolsUtil {

    //将字符串以逗号分隔并添加到数组
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(",");
        return strArray;
    }


    /**
     * 将Map中的key 转成小写
     *
     * @param map
     * @return Map
     */
    public static Map<String, Object> mapToLowerKey(Map<String, Object> map) {
        if (map == null) return null;
        Map<String, Object> newMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Map) {
                val = mapToLowerKey((Map) val);
            }
            newMap.put(entry.getKey().toLowerCase(), val);

        }
        return newMap;
    }

    /**
     * 将列表中 Map对象的 key转小写
     *
     * @param list
     * @return
     */
    public static List<Map> mapToLowerKey(List<Map> list) {
        List<Map> newList = new ArrayList<Map>();
        for (Map map : list) {
            newList.add(mapToLowerKey(map));
        }
        return newList;
    }

    /**
     * 将 List<map>  转成 Map<String,List> 格式，方便前台 echarts图形呈现
     *
     * @param list
     * @return
     */
    public static Map<String, List> listToMap(List<Map> list) {
        Map<String, List> resultMap = new HashMap<>();
        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                List tmpList = resultMap.get(key);
                if (tmpList == null) {
                    tmpList = new ArrayList();
                    resultMap.put(key, tmpList);
                }
                tmpList.add(val);
            }
        }
        return resultMap;
    }


    /**
     * 将Map对象的 key转驼峰格式
     **/
    public static Map<String, Object> mapToHumpKey(Map<String, Object> map) {
        if (map == null) return null;
        Map<String, Object> newMap = new HashMap<String, Object>();
//        map.forEach((k, v) -> { newMap.put(lineToHump(k), v);  });
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            newMap.put(lineToHump(entry.getKey()), entry.getValue());
        }
        return newMap;
    }

    //只支持一层
    public static JSONObject jsonToHumpKey(JSONObject json) {
        if (json == null) {
            return null;
        }
        JSONObject newJson = new JSONObject();
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            Object val = entry.getValue();
            newJson.put(lineToHump(entry.getKey()), val);
        }
        return newJson;
    }

    /**
     * 将列表中 Map对象的 key转驼峰格式
     **/
    public static List<Map> mapToHumpKey(List<Map> list) {
        List<Map> newList = new ArrayList<Map>();
//        list.forEach(val -> newList.add(mapToHumpKey(val)));
        for (Map map : list) {
            newList.add(mapToHumpKey(map));
        }
        return newList;
    }

    ////////////////////////////////驼峰互转格式
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     * is_user  ,IS_USER ,is_USER   ==>isUser
     *
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String)})
     * isUser  ==>  is_user
     * isAUser ==>  is_a_user
     *
     * @param str
     * @return -
     */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线,效率比上面高
     */
    public static String humpToLine2(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static Integer toInteger(Object str) {
        if (str == null) return null;
        if (str instanceof Integer) return (Integer) str;
        try {
            return Integer.parseInt(str.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static String toStr(Object str) {
        try {
            if (str == null) return null;
            return String.valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String processDeptName(String deptName, String deptParentName) {
        String str = (deptParentName != null ? deptParentName : "") + deptName;
        return str.replaceAll("分公司", "");
    }

    public static String trimLongStr(String longStr, int length) {
        if (longStr == null || longStr.equals("") || longStr.length() <= length) return longStr;
        return longStr.substring(0, length);
    }

    public static void main(String[] args) {
        String line = "IS_USER";
        String hump = lineToHump(line);
        System.out.println(hump);
        String line2 = humpToLine("isUCab");
        System.out.println(line2);

    }

}
