package com.luopc.platform.cloud.web.handler;

import com.luopc.platform.cloud.web.exception.BaseException;
import com.luopc.platform.cloud.web.exception.ExceptionCatch;
import com.luopc.platform.cloud.web.exception.log.AppErrorCodeLogger;
import com.luopc.platform.cloud.web.exception.log.AppErrorCodeLoggerFactory;
import com.luopc.platform.common.core.exception.BusinessException;
import com.luopc.platform.common.core.exception.PlatformErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常拦截
 *
 * @author Robin
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final AppErrorCodeLogger LOGGER = AppErrorCodeLoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * json 格式错误 缺少请求体
     */
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> jsonFormatExceptionHandler(Exception e) {
        return ResponseEntity.internalServerError().body(PlatformErrorCode.PARAM_ERROR);
    }

    /**
     * json 格式错误 缺少请求体
     */
    @ResponseBody
    @ExceptionHandler({TypeMismatchException.class, BindException.class})
    public ResponseEntity<?> paramExceptionHandler(Exception e) {
        if (e instanceof BindException) {
            if (e instanceof MethodArgumentNotValidException) {
                List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
                List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseEntity.internalServerError().body(new BusinessException(PlatformErrorCode.PARAM_ERROR, String.join(",", msgList)));
            }

            List<FieldError> fieldErrors = ((BindException) e).getFieldErrors();
            List<String> error = fieldErrors.stream().map(field -> field.getField() + ":" + field.getRejectedValue()).collect(Collectors.toList());
            String errorMsg = PlatformErrorCode.PARAM_ERROR.getMessage() + ":" + error;
            return ResponseEntity.internalServerError().body(new BusinessException(PlatformErrorCode.PARAM_ERROR, errorMsg));
        }
        return ResponseEntity.internalServerError().body(PlatformErrorCode.PARAM_ERROR);
    }

    /**
     * 业务异常
     */
    @ResponseBody
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> businessExceptionHandler(BaseException e) {
        LOGGER.error("Error code: {}, parameters {}", e.getError().getCode(), e.getParams().toArray());
        return ResponseEntity.internalServerError().body(new BusinessException(e.getError(), e.getMessage()));
    }

    /**
     * 其他全部异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> errorHandler(Exception e) {
        return ExceptionCatch.exception(e);
    }

    /**
     * 其他全部异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> errorHandler(Throwable e) {
        return ResponseEntity.internalServerError().body(new BusinessException(PlatformErrorCode.SYSTEM_ERROR, PlatformErrorCode.SYSTEM_ERROR.getMessage()));
    }


}
