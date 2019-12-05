package me.ffis.checkdomain.service;


import me.ffis.checkdomain.model.response.Result;
import me.ffis.checkdomain.model.response.ResultResponse;

/**
 * Created by fanfan on 2019/12/03.
 */
public interface CheckService {

    /**
     * 查询域名的状态
     *
     * @param domain 域名
     * @return 查询结果
     */
    public ResultResponse checkDomain(String domain);

    public ResultResponse checkDomainAndNotify(String domain, String queryKey);
}
