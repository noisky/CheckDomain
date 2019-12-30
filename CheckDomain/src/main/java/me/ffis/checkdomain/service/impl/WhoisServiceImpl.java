package me.ffis.checkdomain.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.ffis.checkdomain.model.WhoisModel;
import me.ffis.checkdomain.model.response.ReponseCode;
import me.ffis.checkdomain.model.response.ResultResponse;
import me.ffis.checkdomain.service.WhoisService;
import me.ffis.checkdomain.util.WhoisUtil;
import org.springframework.stereotype.Service;

/**
 * 域名whois服务
 * Created by fanfan on 2019/12/28.
 */

@Slf4j
@Service
public class WhoisServiceImpl implements WhoisService {

    /**
     * 查询域名的whois信息
     *
     * @param domain 域名
     * @return whois信息
     */
    @Override
    public ResultResponse getDomainWhois(String domain) {
        WhoisModel whoisModel = WhoisUtil.queryWhois(domain);
        /**
         * domain:
         * -1: 不支持的后缀
         * -2: 查询域名不存在
         * -3: 查询超时
         * -4: whois信息解析异常
         */
        if ("-1".equals(whoisModel.getDomain())) {
            return new ResultResponse(ReponseCode.UNSUPPORTED_SUFFIX);
        }
        if ("-2".equals(whoisModel.getDomain())) {
            return new ResultResponse(ReponseCode.DOMAIN_NOT_EXIST, whoisModel.getWhiosReponse());
        }
        if ("-3".equals(whoisModel.getDomain())) {
            return new ResultResponse(ReponseCode.QUERY_TIMEOUT);
        }
        if ("-4".equals(whoisModel.getDomain())) {
            return new ResultResponse(ReponseCode.WHOIS_PARSING_EXCEPTION);
        }
        return new ResultResponse(ReponseCode.QUERY_SUCCESS, whoisModel);
    }
}
