package me.ffis.checkdomain.util.whoisparsers;

import lombok.extern.slf4j.Slf4j;
import me.ffis.checkdomain.exception.ExceptionCast;
import me.ffis.checkdomain.model.WhoisModel;
import me.ffis.checkdomain.model.constant.MessageConstant;
import me.ffis.checkdomain.model.response.ReponseCode;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 域名信息解析
 * 适用于 .COM/.NET/.EDU/.ORG/.ME
 * <p>
 * Created by fanfan on 2019/12/28.
 * <p>
 * Domain Name: BAIDU.COM
 * Registry Domain ID: 11181110_DOMAIN_COM-VRSN
 * Registrar WHOIS Server: whois.markmonitor.com
 * Registrar URL: http://www.markmonitor.com
 * Updated Date: 2019-05-09T04:30:46Z
 * Creation Date: 1999-10-11T11:05:17Z
 * Registry Expiry Date: 2026-10-11T11:05:17Z
 * Registrar: MarkMonitor Inc.
 * Registrar IANA ID: 292
 * Registrar Abuse Contact Email: abusecomplaints@markmonitor.com
 * Registrar Abuse Contact Phone: +1.2083895740
 * Domain Status: clientDeleteProhibited https://icann.org/epp#clientDeleteProhibited
 * Domain Status: clientTransferProhibited https://icann.org/epp#clientTransferProhibited
 * Domain Status: clientUpdateProhibited https://icann.org/epp#clientUpdateProhibited
 * Domain Status: serverDeleteProhibited https://icann.org/epp#serverDeleteProhibited
 * Domain Status: serverTransferProhibited https://icann.org/epp#serverTransferProhibited
 * Domain Status: serverUpdateProhibited https://icann.org/epp#serverUpdateProhibited
 * Name Server: NS1.BAIDU.COM
 * Name Server: NS2.BAIDU.COM
 * Name Server: NS3.BAIDU.COM
 * Name Server: NS4.BAIDU.COM
 * Name Server: NS7.BAIDU.COM
 * DNSSEC: unsigned
 * URL of the ICANN Whois Inaccuracy Complaint Form: https://www.icann.org/wicf/
 * >>> Last update of whois database: 2019-12-28T08:21:24Z <<<
 */
@Slf4j
public class VerisignParser extends AParser {
    private VerisignParser() {
    }

    private static VerisignParser instance = null;

    public static VerisignParser getInstance() {
        if (instance == null) {
            instance = new VerisignParser();
        }
        return instance;
    }

    //定义解析正则
    private final String DOMAINREG = "Domain Name: *(.+)";
    private final String REGISTRARREG = "Registrar: *(.+)";
    private final String EMAILREG = "Registrar Abuse Contact Email: *(.+)";
    private final String PHONEREG = "Registrar Abuse Contact Phone: *(.+)";
    private final String CREATDATEREG = "Creation Date: *(.+)";
    private final String EXPIREDATEREG = "Registry Expiry Date: *(.+)";
    private final String UPDATEDATEREG = "Updated Date: *(.+)";
    private final String WHOISREG = "Registrar WHOIS Server: *(.+)";
    private final String NAMESERVERREG = "Name Server: *(.+)";
    private final String STATUSREG = "Domain Status: *(.+)";
    private Pattern domainPattern = Pattern.compile(DOMAINREG);
    private Pattern registrarPattern = Pattern.compile(REGISTRARREG);
    private Pattern emailPattern = Pattern.compile(EMAILREG);
    private Pattern phonePattern = Pattern.compile(PHONEREG);
    private Pattern createDatePattern = Pattern.compile(CREATDATEREG);
    private Pattern expireDatePattern = Pattern.compile(EXPIREDATEREG);
    private Pattern updateDatePattern = Pattern.compile(UPDATEDATEREG);
    private Pattern whoisServerPattern = Pattern.compile(WHOISREG);
    private Pattern nameServerPattern = Pattern.compile(NAMESERVERREG);
    private Pattern statusPattern = Pattern.compile(STATUSREG);
    //日期格式化
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public WhoisModel parseWhois(String whoisResponse) {
        WhoisModel whoisModel = new WhoisModel();
        try {
            String domain = getFieldValue(getMatchField(domainPattern, whoisResponse), ":");
            whoisModel.setDomain(domain);
            String registrar = getFieldValue(getMatchField(registrarPattern, whoisResponse), ":");
            whoisModel.setRegistrar(registrar);
            String email = getFieldValue(getMatchField(emailPattern, whoisResponse), ":");
            whoisModel.setEmail(email);
            String phone = getFieldValue(getMatchField(phonePattern, whoisResponse), ":");
            whoisModel.setPhone(phone);
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createDate = getFieldValue(getMatchField(createDatePattern, whoisResponse), ":");
            whoisModel.setCreatDate(simpleDateFormat.parse(createDate));
            String expireDate = getFieldValue(getMatchField(expireDatePattern, whoisResponse), ":");
            whoisModel.setExpireDate(simpleDateFormat.parse(expireDate));
            String updateDate = getFieldValue(getMatchField(updateDatePattern, whoisResponse), ":");
            whoisModel.setUpdateDate(simpleDateFormat.parse(updateDate));
            String whoisServer = getFieldValue(getMatchField(whoisServerPattern, whoisResponse), ":");
            whoisModel.setWhoisServer(whoisServer);
            List<String> nameServerMarchs = getMarchs(nameServerPattern, whoisResponse, ":");
            whoisModel.setNameServer(nameServerMarchs);
            List<String> statusMarchs = getMarchs(statusPattern, whoisResponse, ":");
            whoisModel.setDomainStatus(statusMarchs);
        } catch (Exception ex) {
            log.error(MessageConstant.WHOIS_PARSING_EXCEPTION, ex);
            ExceptionCast.cast(ReponseCode.WHOIS_PARSING_EXCEPTION);
        }
        return whoisModel;
    }
}
