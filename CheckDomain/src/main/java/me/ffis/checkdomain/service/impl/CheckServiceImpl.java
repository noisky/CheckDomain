package me.ffis.checkdomain.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.ffis.checkdomain.model.MailTemplateModel;
import me.ffis.checkdomain.model.constant.MessageConstant;
import me.ffis.checkdomain.model.response.CheckDomainCode;
import me.ffis.checkdomain.model.response.ReponseCode;
import me.ffis.checkdomain.model.response.ResultResponse;
import me.ffis.checkdomain.service.CheckService;
import me.ffis.checkdomain.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

/**
 * 域名监测服务
 * Created by fanfan on 2019/12/03.
 */
@Slf4j
@Service
public class CheckServiceImpl implements CheckService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailService mailService;

    @Value("${customize.query.password}")
    private String QUERY_PASSWORD;


    /**
     * 调用阿里云接口查询域名的状态
     *
     * @param domain 域名
     * @return 查询结果
     */
    @Override
    public ResultResponse checkDomain(String domain) {
        //调用查询方法查询域名状态
        Map map = this.queryFromAliyun(domain);
        if (map == null) {
            throw new RuntimeException(MessageConstant.API_PARSING_EXCEPTION);
        }
        //获取查询信息
        String original = (String) map.get("original");
        if (original.contains("210")) {
            //域名可以注册
            return new ResultResponse(CheckDomainCode.DOMAIN_AVAILABLE);
        } else if (original.contains("211")) {
            return new ResultResponse(CheckDomainCode.DOMAIN_NOT_AVAILABLE);
        } else if (original.contains("212")) {
            return new ResultResponse(CheckDomainCode.DOMAIN_INVALID);
        } else if (original.contains("213")) {
            return new ResultResponse(CheckDomainCode.CHECK_TIMEOUT);
        }
        return new ResultResponse(ReponseCode.QUERY_FAIL);
    }

    /**
     * 查询域名状态，并在域名可注册的时候发送邮件通知
     *
     * @param domain   查询域名
     * @param queryKey 查询密码
     * @return 查询结果
     */
    @Override
    public ResultResponse checkDomainAndNotify(String domain, String queryKey) {
        //验证查询key
        if (!queryKey.equals(QUERY_PASSWORD)) {
            return new ResultResponse(ReponseCode.WRONG_QUERY_PASSWORD);
        }
        //调用接口查询域名状态
        Map map = this.queryFromAliyun(domain);
        if (map == null) {
            throw new RuntimeException(MessageConstant.API_PARSING_EXCEPTION);
        }
        //获取查询信息
        String original = (String) map.get("original");
        if (original.contains("210")) { //域名可以注册
            //创建模板信息对象
            MailTemplateModel model = new MailTemplateModel();
            //封装数据
            model.setDomain(domain);
            model.setTime(new Date());
            model.setStatus("Domain name is available 域名可以注册");
            //调用邮件服务异步发送邮件通知
            mailService.sendSimpleMail(model);
            //返回结果
            return new ResultResponse(CheckDomainCode.DOMAIN_AVAILABLE);
        } else if (original.contains("211")) {
            return new ResultResponse(CheckDomainCode.DOMAIN_NOT_AVAILABLE);
        } else if (original.contains("212")) {
            return new ResultResponse(CheckDomainCode.DOMAIN_INVALID);
        } else if (original.contains("213")) {
            return new ResultResponse(CheckDomainCode.CHECK_TIMEOUT);
        }
        return new ResultResponse(ReponseCode.QUERY_FAIL);
    }

    /**
     * 调用阿里云api接口查询进行域名可用性查询
     *
     * @param domain 查询域名
     * @return 查询结果
     */
    Map queryFromAliyun(String domain) {
        //拼接查询地址
        String queryDomain = "http://panda.www.net.cn/cgi-bin/check.cgi?area_domain=" + domain;
        //请求万网接口进行查询
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(queryDomain, Map.class);
        HttpStatus statusCode = forEntity.getStatusCode();
        //判断查询结果
        if (!forEntity.getStatusCode().toString().contains("200") || forEntity.getBody() == null) {
            log.error(MessageConstant.API_PARSING_EXCEPTION);
            return null;
        }
        //返回查询结果
        return forEntity.getBody();
    }
}
