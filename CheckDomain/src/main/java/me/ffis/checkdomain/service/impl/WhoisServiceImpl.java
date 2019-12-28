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
        return new ResultResponse(ReponseCode.QUERY_SUCCESS, whoisModel);
    }
}
