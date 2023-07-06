package com.jnetdata.msp.config;

import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
public class WebmvcConfiguration {

    @Bean
    public FastJsonHttpMessageConverter createJsonConverter() {
        FastJsonHttpMessageConverter fjc = new FastJsonHttpMessageConverterExtension();
        FastJsonConfig fj = createFastJsonConfig();
        fjc.setFastJsonConfig(fj);
        return fjc;
    }

    private FastJsonConfig createFastJsonConfig() {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setSerializerFeatures(
                // 保留map空的字段
                SerializerFeature.WriteMapNullValue,
                // 字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteNullStringAsEmpty,
                // List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,
                // Boolean字段如果为null,输出为false,而非null
                SerializerFeature.WriteNullBooleanAsFalse,
                // 消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
                SerializerFeature.DisableCircularReferenceDetect);
        //fastJsonConfig.setDateFormat("yyyy-MM-dd");

/**
 * 关键代码，实现PropertyPreFilter中的apply()方法
 * 参数说明：
 * 1、serializer：不知道有啥用，没具体研究，有兴趣的小伙伴可自行研究
 * 2、source：需要序列化的对象
 * 3、name：对象中的字段名
 */
        PropertyFilter preFilter = (Object object, String name, Object value) -> {
            if (object == null) {
                return true;
            }
            if(name.equals("children")){
                return value!=null && !((List)value).isEmpty();
            }
            return true;
        };

        // 序列化时修改value
        ValueFilter valueFilter = (object, name, value) -> {
            try {
                if (value == null && object != null &&
                        (Date.class.isAssignableFrom(object.getClass().getDeclaredField(name).getType())
                                || Number.class.isAssignableFrom(object.getClass().getDeclaredField(name).getType())) ) {
                    return "";
                }
            }catch (Exception e) {
                return value;
            }

            return value;
        };
        fastJsonConfig.setSerializeFilters(preFilter, valueFilter);

        return fastJsonConfig;
    }

    public class FastJsonHttpMessageConverterExtension extends FastJsonHttpMessageConverter {

        public FastJsonHttpMessageConverterExtension() {
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
            mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
            setSupportedMediaTypes(mediaTypes);
        }
    }

}
