package me.ffis.checkdomain.controller;

import me.ffis.checkdomain.model.response.ResultResponse;

/**
 * 域名Whois接口
 * Created by fanfan on 2019/12/28.
 */
public interface WhoisControllerApi {

    /**
     * 查询域名的whois信息
     * method:GET
     *
     * @return whois信息
     */
    public ResultResponse checkDomainWhois(String domain);

}
