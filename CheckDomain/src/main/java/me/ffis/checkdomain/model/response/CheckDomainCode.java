package me.ffis.checkdomain.model.response;

import lombok.ToString;

/**
 * 封装查询常量
 * <original>210
 * Domain name is available 表示域名可以注册
 * <original>211
 * Domain name is not available 表示域名已经注册
 * <original>212
 * Domain name is invalid 表示域名参数传输错误
 * <original>213
 * Time out 查询超时
 */
@ToString
public enum CheckDomainCode implements Result {
    DOMAIN_AVAILABLE(true, 210, "域名可以注册"),
    DOMAIN_NOT_AVAILABLE(false, 211, "域名已经注册"),
    DOMAIN_INVALID(false, 212, "域名参数传输错误"),
    CHECK_TIMEOUT(false, 213, "查询超时");

    //操作是否成功
    private boolean flag;
    //操作代码
    private Integer code;
    //提示信息
    private String message;
    //返回数据
    private Object data;

    CheckDomainCode(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean flag() {
        return flag;
    }

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public Object data() {
        return data;
    }
}
