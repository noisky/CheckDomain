package me.ffis.checkdomain.util.whoisparsers;

import me.ffis.checkdomain.model.WhoisModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CN 域名信息解析
 * <p>
 * Created by fanfan on 2019/12/28.
 * <p>
 * Domain Name: 123.cn
 * ROID: 20030312s10001s00055206-cn
 * Domain Status: clientDeleteProhibited
 * Domain Status: clientTransferProhibited
 * Registrant ID: ename_5ew8j1na88
 * Registrant: 厦门皮蛋信息技术服务有限公司
 * Registrant Contact Email: 200855771@qq.com
 * Sponsoring Registrar: 厦门易名科技股份有限公司
 * Name Server: ns4.dns.com
 * Name Server: ns3.dns.com
 * Registration Time: 2003-03-17 12:20:05
 * Expiration Time: 2020-03-17 12:48:36
 * DNSSEC: unsigned
 */
public class CnParser extends AParser {
    private CnParser() {
    }

    private static CnParser instance = null;

    public static CnParser getInstance() {
        if (instance == null) {
            instance = new CnParser();
        }
        return instance;
    }

    //定义cn域名解析正则
    private final String DOMAINREG = "\\s*Domain Name:\\s*[^\\n]+";
    private final String REGISTRARREG = "\\s*Registrant:\\s*[^\\n]+";
    private final String EMAILREG = "\\s*Registrant Contact Email:\\s*[^\\n]+";
    //    private final String PHONEREG = "\\s*Registrar Abuse Contact Phone:\\s*[^\\n]+";
    private final String CREATDATEREG = "\\s*Registration Time:\\s*[^\\n]+";
    private final String EXPIREDATEREG = "\\s*Expiration Time:\\s*[^\\n]+";
    //    private final String UPDATEDATEREG = "\\s*Updated Date:\\s*[^\\n]+";
//    private final String WHOISREG = "\\s*Registrar WHOIS Server:\\s*[^\\n]+";
    private final String NAMESERVERREG = "\\s*Name Server:\\s*[^\\n]+";
    private final String STATUSREG = "\\s*Domain Status:\\s*[^\\n]+";
    private Pattern domainPattern = Pattern.compile(DOMAINREG);
    private Pattern registrarPattern = Pattern.compile(REGISTRARREG);
    private Pattern emailPattern = Pattern.compile(EMAILREG);
    //    private Pattern phonePattern = Pattern.compile(PHONEREG);
    private Pattern createDatePattern = Pattern.compile(CREATDATEREG);
    private Pattern expireDatePattern = Pattern.compile(EXPIREDATEREG);
    //    private Pattern updateDatePattern = Pattern.compile(UPDATEDATEREG);
//    private Pattern whoisServerPattern = Pattern.compile(WHOISREG);
    private Pattern nameServerPattern = Pattern.compile(NAMESERVERREG);
    private Pattern statusPattern = Pattern.compile(STATUSREG);
    //日期格式化
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public WhoisModel parseWhois(String whoisResponse) {
        WhoisModel whoisModel = new WhoisModel();
        try {
            String domain = getFieldValue(getMatchField(domainPattern, whoisResponse), ":");
            whoisModel.setDomain(domain);
            String registrar = getFieldValue(getMatchField(registrarPattern, whoisResponse), ":");
            whoisModel.setRegistrar(registrar);
            String email = getFieldValue(getMatchField(emailPattern, whoisResponse), ":");
            whoisModel.setEmail(email);
//            String phone = getFieldValue(getMatchField(phonePattern, whoisResponse), ":");
//            whoisModel.setPhone(phone);
            String createDate = getFieldValue(getMatchField(createDatePattern, whoisResponse), ":");
            whoisModel.setCreatDate(simpleDateFormat.parse(createDate).getTime());
            String expireDate = getFieldValue(getMatchField(expireDatePattern, whoisResponse), ":");
            whoisModel.setExpireDate(simpleDateFormat.parse(expireDate).getTime());
//            String updateDate = getFieldValue(getMatchField(updateDatePattern, whoisResponse), ":");
//            whoisModel.setUpdateDate(simpleDateFormat.parse(updateDate).getTime());
//            String whoisServer = getFieldValue(getMatchField(whoisServerPattern, whoisResponse), ":");
//            whoisModel.setWhoisServer(whoisServer);
            List<String> nameServerMarchs = getMarchs(nameServerPattern, whoisResponse, ":");
            whoisModel.setNameServer(nameServerMarchs);
            List<String> statusMarchs = getMarchs(statusPattern, whoisResponse, ":");
            whoisModel.setDomainStatus(statusMarchs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return whoisModel;
    }
}
