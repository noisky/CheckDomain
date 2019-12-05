package me.ffis.checkdomain.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import me.ffis.checkdomain.model.MailTemplateModel;
import me.ffis.checkdomain.model.response.AliYunResponse;
import me.ffis.checkdomain.model.response.CheckDomainCode;
import me.ffis.checkdomain.model.response.ReponseCode;
import me.ffis.checkdomain.model.response.ResultResponse;
import me.ffis.checkdomain.service.CheckService;
import me.ffis.checkdomain.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;

/**
 * 域名监测服务
 * Created by fanfan on 2019/12/03.
 */
@Service
public class CheckServiceImpl implements CheckService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailService mailService;

    @Value("${customize.query.password}")
    private String queryPassword;


    /**
     * 调用阿里云接口查询域名的状态
     *
     * @param domain 域名
     * @return 查询结果
     */
    @Override
    public ResultResponse checkDomain(String domain) {
        AliYunResponse result = this.queryFromAliyun(domain);
        if (result == null) {
            throw new RuntimeException("调用阿里云接口查询失败");
        }
        //获取查询信息
        String original = result.getOriginal();
        if (original.contains("210")) {
            //域名可以注册
            return new ResultResponse(CheckDomainCode.DOMAIN_AVAILABLE);
        } else if (original.contains("211")) {
            return new ResultResponse(CheckDomainCode.DOMAIN_NOTAVAILABLE);
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
        if (!queryKey.equals(queryPassword)) {
            return new ResultResponse(ReponseCode.WRONG_QUERY_PASSWORD);
        }
        //调用接口查询
        AliYunResponse result = this.queryFromAliyun(domain);
        if (result == null) {
            throw new RuntimeException("调用阿里云接口查询失败");
        }
        //获取查询信息
        String original = result.getOriginal();
        if (original.contains("210")) {
            //创建模板信息对象
            MailTemplateModel model = new MailTemplateModel();
            model.setDomain(domain);
            model.setTime(new Date());
            model.setStatus("Domain name is available 表示域名可以注册");
            //调用邮件服务发送邮件通知
            mailService.sendSimpleMail(model);
            //域名可以注册
            return new ResultResponse(CheckDomainCode.DOMAIN_AVAILABLE);
        } else if (original.contains("211")) {
            return new ResultResponse(CheckDomainCode.DOMAIN_NOTAVAILABLE);
        }
        return new ResultResponse(ReponseCode.QUERY_FAIL);
    }

    /**
     * 调用阿里云api接口查询进行域名可用性查询
     *
     * @param domain 查询域名
     * @return 查询结果
     */
    public AliYunResponse queryFromAliyun(String domain) {
        //拼接查询地址
        String queryDomain = "http://panda.www.net.cn/cgi-bin/check.cgi?area_domain=" + domain;
        //请求万网接口进行查询
        String xml = restTemplate.getForObject(queryDomain, String.class);
        if (xml == null) {
            logger.error("api返回结果为null");
            return null;
        }
        //创建XmlMapper解析对象
        ObjectMapper objectMapper = new XmlMapper();
        AliYunResponse result;
        try {
            //解析xml为Result对象
            result = objectMapper.readValue(xml, AliYunResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("xml解析异常", e);
            return null;
        }
        //返回查询结果
        return result;
    }
}
