package org.lxy.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 需要同时有无参和全参构造器,不然jackson序列化会失败
 * @param <T>
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult<T> implements Serializable{
    private static final String SUCCESS_CODE = ApiCode.OK.getCode();

    private boolean success;
    private String code;
    private String message;
    private T data;

    public static ApiResult success() {
        return ApiResult.builder().success(true).code(SUCCESS_CODE).build();
    }

    public static <T> ApiResult success(final T data) {
        return ApiResult.builder().success(true).code(SUCCESS_CODE).data(data).build();
    }

    public static <T> ApiResult success(final String message, final T data) {
        return ApiResult.builder().success(true).code(SUCCESS_CODE).message(message).data(data).build();
    }

    public static ApiResult error(final String errorCode, final String message) {
        return ApiResult.builder().success(false).code(errorCode).message(message).build();
    }

    public static <T> ApiResult error(final String errorCode, final String message, final T data) {
        return ApiResult.builder().success(false).code(errorCode).message(message).data(data).build();
    }
}
