package me.ffis.checkdomain.controller;

import me.ffis.checkdomain.model.response.ResultResponse;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 域名状态查询接口
 * Created by fanfan on 2019/12/05.
 */
public interface CheckControllerApi {

    /**
     * 查看域名状态
     *
     * @param domain 查看的域名
     * @return 域名状态
     */
    public ResultResponse checkDomain(String domain);

    /**
     * 查询域名状态，并在域名可注册的时候发送邮件通知
     *
     * @param domain   查询域名
     * @param querykey 查询密码
     * @return 查询结果
     */
    public ResultResponse checkDomainAndNotify(String domain, String querykey);
}
