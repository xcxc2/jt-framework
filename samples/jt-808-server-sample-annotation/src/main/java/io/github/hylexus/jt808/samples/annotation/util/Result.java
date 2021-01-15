package io.github.hylexus.jt808.samples.annotation.util;

import com.alibaba.fastjson.JSON;

/**
 * 返回状态封装类
 * Created by zhangcheng on 2018/2/1.
 */
public class Result<T> {

    /**
     * 返回请求响应
     */
    private boolean success;

    /**
     * 返回状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 携带数据
     */
    private T data;

    public static Result build(CodeEnum codeEnum) {
        Result result = new Result();
        result.setCode(codeEnum.code);
        result.setMessage(codeEnum.text);

        if (CodeEnum.SUCCESS == codeEnum) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    public static  Result build(CodeEnum codeEnum, String message) {
        Result result = Result.build(codeEnum);
        if (message != null && !"".equals(message)) {
            result.setMessage(message);
        }
        return result;
    }

    public static <T> Result<T> build(CodeEnum codeEnum, T data) {
        Result<T> result = Result.build(codeEnum);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> build(CodeEnum codeEnum, String message, T data) {
        Result<T> result = Result.build(codeEnum);
        if (message != null && !"".equals(message)) {
            result.setMessage(message);
        }
        result.setData(data);
        return result;
    }


    public static Result done() {
        return Result.build(CodeEnum.SUCCESS);
    }

    public static Result fail() {
        return Result.build(CodeEnum.ERROR);
    }

    public static Result doneMessage(String message) {
        return Result.build(CodeEnum.SUCCESS, message);
    }

    public static Result failMessage(String message) {
        return Result.build(CodeEnum.ERROR, message);
    }

    public static <T> Result<T> doneData(T data) {
        return Result.build(CodeEnum.SUCCESS, data);
    }

    public static <T> Result<T> failData(T data) {
        return Result.build(CodeEnum.ERROR, data);

    }

    public static <T> Result<T> done(String message, T data) {
        return Result.build(CodeEnum.SUCCESS, message, data);

    }

    public static <T> Result<T> fail(String message, T data) {
        return Result.build(CodeEnum.ERROR, message, data);

    }

    Result() {
    }

    public Result(boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public enum CodeEnum {
        SUCCESS(1000, "[系统]操作成功"),
        ERROR(1001, "[系统]操作失败"),
        EXCEPTION(1002,"[系统]服务异常"),
        NOT_LOGIN(1100,"[系统]未登录");

        private Integer code;
        private String text;

        CodeEnum(Integer code, String text) {
            this.code = code;
            this.text = text;
        }

        public Integer getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }
}
