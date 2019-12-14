package me.ffis.checkdomain.exception;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import me.ffis.checkdomain.model.response.ReponseCode;
import me.ffis.checkdomain.model.response.Result;
import me.ffis.checkdomain.model.response.ResultResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常捕获类
 * Created by fanfan on 2019/11/27.
 */

@Slf4j
@RestControllerAdvice
//@RequestMapping(produces = {"application/json;charset=UTF-8"})
public class ExceptionCatch {

    //使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>, Result> EXCEPTIONS;
    //使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>, Result> builder = ImmutableMap.builder();


    //捕获CustomException异常
    @ExceptionHandler(CustomException.class)
    public ResultResponse customException(CustomException e) {
        log.error("catch exception : {}\r\nexception: ", e.getMessage(), e);
        Result resultCode = e.getResultCode();
        return new ResultResponse(resultCode);
    }

    //捕获Exception异常
    @ExceptionHandler(Exception.class)
    public ResultResponse exception(Exception e) {
        log.error("catch exception : {}\r\nexception: ", e.getMessage(), e);
        if (EXCEPTIONS == null)
            EXCEPTIONS = builder.build();
        final Result resultCode = EXCEPTIONS.get(e.getClass());
        final ResultResponse responseResult;
        if (resultCode != null) {
            responseResult = new ResultResponse(resultCode);
        } else {
            responseResult = new ResultResponse(ReponseCode.SERVER_ERROR);
        }
        return responseResult;
    }

    static {
        //在这里加入一些基础的异常类型判断
        builder.put(HttpMessageNotReadableException.class, ReponseCode.ILLEGAL_PARAMETER);
    }
}
