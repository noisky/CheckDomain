package me.ffis.checkdomain.exception;

import me.ffis.checkdomain.model.response.Result;

/**
 * Created by fanfan on 2019/11/27.
 */
public class CustomException extends RuntimeException {

    private Result resultCode;

    public CustomException(Result resultCode) {
        //异常信息为错误代码+异常信息
        super("错误代码：" + resultCode.code() + " 错误信息：" + resultCode.message());
        this.resultCode = resultCode;
    }

    public Result getResultCode() {
        return this.resultCode;
    }
}
