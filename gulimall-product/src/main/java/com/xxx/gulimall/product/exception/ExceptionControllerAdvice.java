package com.xxx.gulimall.product.exception;


import com.xxx.common.utils.R;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> map = new HashMap<>();

        for (FieldError fieldError: fieldErrors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return R.error(400, "valid not pass").put("error", map);

    }

//    @ExceptionHandler(value = Throwable.class)
//    public R exceptionHandle(Throwable t) {
//        return R.error(400, "unknown error").put("error", t.getMessage());
//    }
}
