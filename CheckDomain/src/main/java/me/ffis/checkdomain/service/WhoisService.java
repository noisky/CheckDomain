package me.ffis.checkdomain.service;


import me.ffis.checkdomain.model.response.ResultResponse;

/**
 * Created by fanfan on 2019/12/28.
 */
public interface WhoisService {

    /**
     * 查询域名的whois信息
     *
     * @param domain 域名
     * @return whois信息
     */
    public ResultResponse getDomainWhois(String domain);
}
