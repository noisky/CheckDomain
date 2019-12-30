package me.ffis.checkdomain.model.response;

import lombok.ToString;

/**
 * 枚举类封装返回常量
 */
@ToString
public enum ReponseCode implements Result {
    QUERY_SUCCESS(true, 100, "查询成功"),
    QUERY_FAIL(false, 101, "查询失败"),
    ILLEGAL_PARAMETER(false, 102, "非法参数"),
    WRONG_QUERY_PASSWORD(false, 103, "查询密码错误"),
    API_RETURN_NULL(false, 105, "Api请求异常"),
    XML_PARSING_EXCEPTION(false, 106, "xml解析异常"),
    WHOIS_PARSING_EXCEPTION(false, 107, "whois信息解析异常"),
    EMAIL_ADDR_ERROR(false, 108, "邮箱格式不正确"),
    DOMAIN_NOT_EXIST(false, 110, "查询域名不存在"),
    UNSUPPORTED_SUFFIX(false, 111, "不支持的后缀"),
    QUERY_TIMEOUT(false, 400, "查询超时，请稍后重试"),
    SERVER_ERROR(false, 999, "系统繁忙，请稍后重试");

    //操作是否成功
    private boolean flag;
    //操作代码
    private Integer code;
    //提示信息
    private String message;
    //返回信息
    private Object data;

    private ReponseCode(boolean flag, Integer code, String message) {
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
