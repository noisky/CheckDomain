package me.ffis.checkdomain.util.whoisparsers;

import java.io.Serializable;

/**
 * Created by dell on 2017/11/14.
 */
public class WhoisParserFactory implements Serializable {
    private static WhoisParserFactory whoisParserFactory = null;

    private WhoisParserFactory() {
    }

    public static WhoisParserFactory getInstance() {
        if (whoisParserFactory == null) {
            whoisParserFactory = new WhoisParserFactory();
        }
        return whoisParserFactory;
    }

    public AParser getParser(String whoisServer) {
        if (whoisServer.equals("whois.internic.net")) {
            return VerisignParser.getInstance();
        } else if (whoisServer.equals("whois.cnnic.cn")) {
            return CnParser.getInstance();
        }
        return VerisignParser.getInstance();
    }
}
