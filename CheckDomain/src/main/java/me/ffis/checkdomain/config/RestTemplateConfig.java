package me.ffis.checkdomain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * RestTemplate 配置类
 * Created by fanfan on 2019/12/07.
 */

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        //创建RestTemplate对象，并且使用okHttp客户端
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        //先获取到converter列表
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            //让jsonConverter支持对text/plain的解析
            if (converter instanceof MappingJackson2XmlHttpMessageConverter) {
                try {
                    //先将原先支持的MediaType列表拷出
                    List<MediaType> mediaTypeList = new ArrayList<>(converter.getSupportedMediaTypes());
                    //加入对text/plain的支持
                    mediaTypeList.add(MediaType.TEXT_PLAIN);
                    //将已经加入了text/plain的MediaType支持列表设置为其支持的媒体类型列表
                    ((MappingJackson2XmlHttpMessageConverter) converter).setSupportedMediaTypes(mediaTypeList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return restTemplate;
    }
}
