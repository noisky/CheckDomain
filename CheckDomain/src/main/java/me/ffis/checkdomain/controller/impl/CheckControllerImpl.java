package me.ffis.checkdomain.controller.impl;

import me.ffis.checkdomain.controller.CheckControllerApi;
import me.ffis.checkdomain.model.response.ResultResponse;
import me.ffis.checkdomain.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 域名状态查询实现
 * Created by fanfan on 2019/12/03.
 */

@RestController
@RequestMapping("/checkstatus")
public class CheckControllerImpl implements CheckControllerApi {

    @Autowired
    CheckService checkServiceImpl;

    /**
     * 查看域名状态
     *
     * @param domain 查看的域名
     * @return 域名状态
     */
    @GetMapping(value = "/domain/{name}", produces = {"application/json;charset=UTF-8"})
    public ResultResponse checkDomain(@PathVariable("name") String domain) {
        return checkServiceImpl.checkDomain(domain);
    }

    /**
     * 查询域名状态，并在域名可注册的时候发送邮件通知
     *
     * @param domain   查询域名
     * @param querykey 查询密码
     * @return 查询结果
     */
    @GetMapping(value = "/domain/{name}/{querykey}", produces = {"application/json;charset=UTF-8"})
    public ResultResponse checkDomainAndNotify(@PathVariable("name") String domain, @PathVariable("querykey") String querykey) {
        return checkServiceImpl.checkDomainAndNotify(domain, querykey);
    }

}
