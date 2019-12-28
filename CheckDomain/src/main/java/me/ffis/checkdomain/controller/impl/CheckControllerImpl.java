package me.ffis.checkdomain.controller.impl;

import me.ffis.checkdomain.controller.CheckControllerApi;
import me.ffis.checkdomain.model.response.ResultResponse;
import me.ffis.checkdomain.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 域名状态查询实现
 * Created by fanfan on 2019/12/03.
 */

@RestController
public class CheckControllerImpl implements CheckControllerApi {

    @Autowired
    CheckService checkServiceImpl;

    /**
     * 查看域名状态
     * method:GET
     *
     * @param domain 查看的域名
     * @return 域名状态
     */
    @Override
    @GetMapping(value = "/domain/{name}", produces = {"application/json;charset=UTF-8"})
    public ResultResponse checkDomain(@PathVariable("name") String domain) {
        return checkServiceImpl.checkDomain(domain);
    }

    /**
     * 查询域名状态，并在域名可注册的时候发送邮件通知
     * method:GET
     *
     * @param domain   查询域名
     * @param email    接受通知的邮箱
     * @param querykey 查询密码
     * @return 查询结果
     */
    @Override
    @GetMapping(value = "/domain/{name}/{email}/{querykey}", produces = {"application/json;charset=UTF-8"})
    public ResultResponse checkDomainAndNotify(@PathVariable("name") String domain, @PathVariable("email") String email, @PathVariable("querykey") String querykey) {
        return checkServiceImpl.checkDomainAndNotify(domain, email, querykey);
    }

    /**
     * 查询域名状态，并在域名可注册的时候发送邮件通知
     * method:POST
     *
     * @param domain   查询域名
     * @param email    接受通知的邮箱
     * @param querykey 查询密码
     * @return 查询结果
     */
    @Override
    @PostMapping(value = "/domain/{name}", produces = {"application/json;charset=UTF-8"})
    public ResultResponse checkDomainAndNotifyWithPost(@PathVariable("name") String domain, String email, String querykey) {
        return checkServiceImpl.checkDomainAndNotify(domain, email, querykey);
    }
}
