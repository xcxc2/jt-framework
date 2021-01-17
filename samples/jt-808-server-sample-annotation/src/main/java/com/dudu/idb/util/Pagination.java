package com.dudu.idb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Inspur Research
 */
public class Pagination {

    private static Logger logger = LoggerFactory.getLogger(Pagination.class);


    private int currentPage;//第几页
    private int pageSize;//每页有几条数据

    public Pagination(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public static Pagination transform(Map requestJson) {

        Integer currentPage = 1;
        Integer pageSize = 10;
        try {
            currentPage = Integer.parseInt(requestJson.get("currentPage") != null ? String.valueOf(requestJson.get("currentPage")) : "1");
            pageSize = Integer.parseInt(requestJson.get("pageSize") != null ? String.valueOf(requestJson.get("pageSize")) : "0");
        } catch (NumberFormatException e) {
            logger.warn("分页模型转换失败,使用默认分页");
        }

        if (currentPage <= 0) {
            currentPage = 1;
        }

        if (pageSize <= 0) {
            pageSize = 10;
        }

        return new Pagination(currentPage, pageSize);
    }


    public static Pagination transform(Integer currentPage, Integer pageSize) {
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }

        return new Pagination(currentPage, pageSize);
    }

    public static Pagination transform(String currentPage, String pageSize) {

        Integer pageNew = 1;
        Integer pageSizeNew = 10;
        try {
            pageNew = Integer.parseInt(currentPage != null && currentPage.length() > 0 ? currentPage : "1");
            pageSizeNew = Integer.parseInt(pageSize != null && pageSize.length() > 0 ? pageSize : "0");
        } catch (NumberFormatException e) {
            logger.warn("分页模型转换失败,使用默认分页");
        }

        if (pageNew == null || pageNew <= 0) {
            pageNew = 1;
        }

        if (pageSizeNew == null || pageSizeNew <= 0) {
            pageSizeNew = 10;
        }

        return new Pagination(pageNew, pageSizeNew);
    }
}
