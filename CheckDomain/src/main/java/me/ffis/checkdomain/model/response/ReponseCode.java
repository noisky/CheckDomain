package me.ffis.checkdomain.model.response;

import lombok.ToString;

/**
 * 封装返回常量
 */
@ToString
public enum ReponseCode implements Result {
    QUERY_SUCCESS(true, 100, "查询成功"),
    QUERY_FAIL(false, 101, "查询失败"),
    ILLEGAL_PARAMETER(false, 102, "非法参数"),
    WRONG_QUERY_PASSWORD(false, 103, "查询密码错误"),
    API_RETURN_NULL(false, 105, "Api请求异常"),
    PARSING_EXCEPTION(false, 106, "xml解析异常"),
    EMAIL_ADDR_ERROR(false, 107, "邮箱格式不正确"),
    SERVER_ERROR(false, 999, "系统繁忙，请稍后重试");

    //操作是否成功
    private boolean flag;
    //操作代码
    private Integer code;
    //提示信息
    private String message;

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
}
