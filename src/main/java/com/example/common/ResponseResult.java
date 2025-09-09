package com.example.common;

import lombok.Data;
import java.io.Serializable;

@Data
public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "成功", data);
    }

    public static <T> ResponseResult<T> success(String msg, T data) {
        return new ResponseResult<>(200, msg, data);
    }

    public static <T> ResponseResult<T> success(String msg) {
        return new ResponseResult<>(200, msg, null);
    }

    // 错误响应
    public static <T> ResponseResult<T> error(String msg) {
        return new ResponseResult<>(500, msg, null);
    }

    public static <T> ResponseResult<T> error(Integer code, String msg) {
        return new ResponseResult<>(code, msg, null);
    }
}