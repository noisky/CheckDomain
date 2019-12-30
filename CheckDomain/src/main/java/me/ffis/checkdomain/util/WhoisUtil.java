package me.ffis.checkdomain.util;

import lombok.extern.slf4j.Slf4j;
import me.ffis.checkdomain.exception.ExceptionCast;
import me.ffis.checkdomain.model.WhoisModel;
import me.ffis.checkdomain.model.response.ReponseCode;
import me.ffis.checkdomain.model.response.ResultResponse;
import me.ffis.checkdomain.util.whoisparsers.WhoisParserFactory;
import org.apache.commons.net.whois.WhoisClient;
import org.parboiled.common.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 域名whois查询工具类
 * Created by fanfan on 2019/12/28.
 */

@Slf4j
public class WhoisUtil implements Serializable {
    private static List<Tuple2<String, String>> domainServerList = new ArrayList<Tuple2<String, String>>();

    //加载域名whois服务器
    static {
        String ipMapStr = ".com       whois.internic.net\n" +
                ".net          whois.internic.net\n" +
                ".edu          whois.internic.net\n" +
                ".org          whois.pir.org\n" +
                ".me          whois.nic.me\n" +
                ".cn           whois.cnnic.cn";
        String[] lines = ipMapStr.split("\n");
        for (String line : lines) {
            line = line.replaceAll("[\\s]+", " ");
            String[] items = line.split(" ");
            domainServerList.add(new Tuple2<String, String>(items[0], items[1]));
        }
    }

    public static WhoisModel queryWhois(String url) {
        String host = url;
        WhoisClient whoisClient = null;
        WhoisModel whoisModel = new WhoisModel();
        String curDoaminServer = null;
        try {
            //创建whois客户端
            whoisClient = new WhoisClient();
            //进行host判断，拿到host主机名
            if (url.startsWith("http://") || url.startsWith("https://")) {
                host = new URL(url).getHost();
            }
            if (host != null && host.startsWith("www.")) {
                host = host.substring(4);
            }
            //遍历拿到域名的whois服务器
            for (Tuple2<String, String> tuple2 : domainServerList) {
                if (host.endsWith(tuple2.a)) {
                    curDoaminServer = tuple2.b;
                    break;
                }
            }
            //如果域名的后缀不在列表中，返回null
            if (curDoaminServer == null) {
                ExceptionCast.cast(ReponseCode.UNSUPPORTED_SUFFIX);
            }
            //设置whois客户端的whois查询服务器
            whoisClient.connect(curDoaminServer);
            //进行whois查询
            String result = whoisClient.query(host);
            if (result.contains("No match") || result.contains("NOT FOUND")) {
                whoisModel.setWhiosReponse(result);
                ExceptionCast.cast(new ResultResponse(ReponseCode.DOMAIN_NOT_EXIST, whoisModel));
            }
            //解析查询结果
            whoisModel = WhoisParserFactory.getInstance().getParser(curDoaminServer).parseWhois(result);
            //将whois查询的返回值注入到模型对象中
            whoisModel.setWhiosReponse(result);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            ExceptionCast.cast(ReponseCode.QUERY_TIMEOUT);
        } finally {
            if (whoisClient != null) {
                try {
                    whoisClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //返回查询结果
        return whoisModel;
    }
}
