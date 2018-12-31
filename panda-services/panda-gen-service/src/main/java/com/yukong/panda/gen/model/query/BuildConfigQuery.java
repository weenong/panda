package com.yukong.panda.gen.model.query;

import com.yukong.panda.gen.model.config.BaseConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "")
@PropertySource("classpath:generator.properties")
public class BuildConfigQuery extends BaseConfig{
}
