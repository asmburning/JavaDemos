package org.lxy.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResult<T> {
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
