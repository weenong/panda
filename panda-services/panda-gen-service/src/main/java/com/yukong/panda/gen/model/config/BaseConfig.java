package com.yukong.panda.gen.model.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: yukong
 * @date: 2018/11/8 13:54
 */
@Data
public class BaseConfig {

    /**
     * entity包名
     */
    private String packageName;
    /**
     * model包名
     */
    private String modelPackageName;

    /**
     * query类的包名
     */
    private String queryPackageName;

    /**
     * controller类的包名
     */
    private String controllerPackageName;

    /**
     * service类包名
     */
    private String servicePackageName;

    /**
     * serviceApi类包名
     */
    private String serviceApiPackageName;

    /**
     * dao的包名
     */
    private String daoPackageName;

    /**
     * 作者名称
     */
    private String authorName;

}
