package me.ffis.checkdomain.model.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * 用于解析阿里云api接口返回的xml数据
 * Created by fanfan on 2019/12/03.
 */

@Data
@JacksonXmlRootElement(localName = "property")
public class AliYunResponse {
    @JacksonXmlProperty(localName = "returncode")
    private Integer returncode;

    @JacksonXmlProperty(localName = "key")
    private String key;

    @JacksonXmlProperty(localName = "original")
    private String original;
}
