package com.nexta1.core.interceptor.exception;


import com.google.common.util.concurrent.UncheckedExecutionException;
import com.nexta1.common.core.dto.ResponseDTO;
import com.nexta1.common.exception.ApiException;
import com.nexta1.common.exception.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author nexta1
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionInterceptor {

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseDTO<?> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.error("请求地址'{}',权限校验失败'{}'", request.getRequestURI(), e.getMessage());
        return ResponseDTO.fail(ErrorCode.Business.NO_PERMISSION_TO_OPERATE);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseDTO<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                              HttpServletRequest request) {
        log.error("请求地址'{}',不支持'{}'请求", request.getRequestURI(), e.getMethod());
        return ResponseDTO.fail(ErrorCode.Client.COMMON_REQUEST_METHOD_INVALID, e.getMethod());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ApiException.class)
    public ResponseDTO<?> handleServiceException(ApiException e) {
        log.error(e.getMessage(), e);
        if (e.getErrorCode() == ErrorCode.Internal.DB_INTERNAL_ERROR) {
            return ResponseDTO.fail(e.getErrorCode(), "请联系管理员查看错误日志");
        }
        return ResponseDTO.fail(e);
    }

    /**
     * 捕获缓存类当中的错误
     */
    @ExceptionHandler(UncheckedExecutionException.class)
    public ResponseDTO<?> handleServiceException(UncheckedExecutionException e) {
        log.error(e.getMessage(), e);
        return ResponseDTO.fail(ErrorCode.Internal.GET_CACHE_FAILED);
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseDTO<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生未知异常.", request.getRequestURI(), e);
        return ResponseDTO.fail(ErrorCode.Internal.UNKNOWN_ERROR);
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseDTO<?> handleException(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生系统异常.", request.getRequestURI(), e);
        return ResponseDTO.fail(ErrorCode.Internal.UNKNOWN_ERROR);
    }

    /**
     * 自定义验证异常 基本类型
     */
    @ExceptionHandler(BindException.class)
    public ResponseDTO<?> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ResponseDTO.fail(ErrorCode.Client.COMMON_REQUEST_PARAMETERS_INVALID, message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseDTO.fail(ErrorCode.Client.COMMON_REQUEST_PARAMETERS_INVALID, message);
    }


}
