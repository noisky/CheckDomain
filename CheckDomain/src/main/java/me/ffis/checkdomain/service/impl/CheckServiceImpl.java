package me.ffis.checkdomain.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.ffis.checkdomain.exception.ExceptionCast;
import me.ffis.checkdomain.model.LogFileName;
import me.ffis.checkdomain.model.MailTemplateModel;
import me.ffis.checkdomain.model.constant.MessageConstant;
import me.ffis.checkdomain.model.response.CheckDomainCode;
import me.ffis.checkdomain.model.response.ReponseCode;
import me.ffis.checkdomain.model.response.Result;
import me.ffis.checkdomain.model.response.ResultResponse;
import me.ffis.checkdomain.service.CheckService;
import me.ffis.checkdomain.service.MailService;
import me.ffis.checkdomain.util.LoggerUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
            return new ResultResponse(ReponseCode.SERVER_ERROR);
        }
        //获取查询信息
        //ExceptionCast.cast(ReponseCode.SERVER_ERROR);
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
    public ResultResponse checkDomainAndNotify(String domain, String email, String queryKey) {
        //验证邮箱格式
        //定义正则
        String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        //进行邮箱格式验证
        boolean matches = Pattern.matches(EMAIL_REGEX, email);
        //判断邮箱验证结果
        if (!matches) {
            return new ResultResponse(ReponseCode.EMAIL_ADDR_ERROR);
        }
        //验证查询key
        if (!queryKey.equals(QUERY_PASSWORD)) {
            return new ResultResponse(ReponseCode.WRONG_QUERY_PASSWORD);
        }
        //调用接口查询域名状态
        Map map = this.queryFromAliyun(domain);
        /*HashMap<String, String> map = new HashMap<>();
        map.put("original", "210");*/
        if (map == null) {
            return new ResultResponse(ReponseCode.SERVER_ERROR);
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
            //请求发送邮件
            String result = this.requestSendMail(model, email);
            //返回结果
            return new ResultResponse(true, 210, "域名可以注册，" + result);
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
        ResponseEntity<Map> forEntity = null;
        try {
            forEntity = restTemplate.getForEntity(queryDomain, Map.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            log.error(MessageConstant.API_PARSING_EXCEPTION, e);
            return null;
        }
        HttpStatus statusCode = forEntity.getStatusCode();
        //判断查询结果
        if (!forEntity.getStatusCode().toString().contains("200") || forEntity.getBody() == null) {
            log.error(MessageConstant.API_PARSING_EXCEPTION);
            return null;
        }
        //返回查询结果
        return forEntity.getBody();
    }

    private long timeStamp = this.getNowTime();
    private static final long interval = 1000 * 60 * 60 * 12; //12h
    //private static final long interval = 1000 * 5; //5s
    private HashMap<String, Integer> sendCountMap = new HashMap<String, Integer>();

    /**
     * 限制12h内邮件发送次数
     *
     * @param model 发送邮件的数据模型
     */
    private String requestSendMail(MailTemplateModel model, String email) {
        Logger maillogger = LoggerUtils.Logger(LogFileName.MAIL_LOGS);
        //获取当前时间
        long nowTime = this.getNowTime();
        if (nowTime < timeStamp + interval) {
            //获取该域名的邮件发送次数
            Integer sendCount = sendCountMap.get(model.getDomain());
            //如果没有发过邮件则发送邮件并记录次数
            if (sendCount == null) {
                //调用异步请求发送邮件
                mailService.sendSimpleMail(model, email);
                //记录该域名的邮件发送次数
                sendCountMap.put(model.getDomain(), 1);
                return "发送了第1封邮件进行通知";
            }
            if (sendCount < 3) {
                //调用异步请求发送邮件
                mailService.sendSimpleMail(model, email);
                //计数器+1
                sendCountMap.put(model.getDomain(), sendCount + 1);
                return "发送了第" + (sendCount + 1) + "封邮件进行通知";
            }
            //当天邮件发送次数已达到限制
            maillogger.info("域名：" + model.getDomain() + " 的邮件发送已达到限制，12小时后重试");
            return "该域名当天邮件发送次数已达到上限";
        }
        //超时后重置时间戳和计数器
        timeStamp = this.getNowTime();
        sendCountMap.clear();
        //调用异步请求发送邮件
        mailService.sendSimpleMail(model, email);
        //记录该域名的邮件发送次数
        sendCountMap.put(model.getDomain(), 1);
        return "发送了第1封邮件进行通知";
    }

    /**
     * 获取当前时间方法
     *
     * @return 当前的时间
     */
    private long getNowTime() {
        return System.currentTimeMillis();
    }
}
