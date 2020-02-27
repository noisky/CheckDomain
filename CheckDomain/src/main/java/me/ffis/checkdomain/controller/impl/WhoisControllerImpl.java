package me.ffis.checkdomain.controller.impl;

import me.ffis.checkdomain.controller.WhoisControllerApi;
import me.ffis.checkdomain.model.response.ReponseCode;
import me.ffis.checkdomain.model.response.ResultResponse;
import me.ffis.checkdomain.service.WhoisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 域名状态查询实现
 * Created by fanfan on 2019/12/03.
 */

@CrossOrigin //开启跨域请求
@RestController
public class WhoisControllerImpl implements WhoisControllerApi {

    @Autowired
    WhoisService whoisService;

    /**
     * 查询域名的whois信息
     * method:GET
     *
     * @return whois信息
     */
    @Override
    @GetMapping(value = "/whois/{domain}")
    public ResultResponse checkDomainWhois(@PathVariable("domain") String doamin) {
        if (doamin != null && !"".equals(doamin)) {
            return whoisService.getDomainWhois(doamin);
        }
        return new ResultResponse(ReponseCode.ILLEGAL_PARAMETER);
    }

    /*@GetMapping(value = "/test")
    public ResultResponse checkDomainWhois() {
        ExceptionCast.cast(ReponseCode.EMAIL_ADDR_ERROR);
        return null;
    }*/

}
