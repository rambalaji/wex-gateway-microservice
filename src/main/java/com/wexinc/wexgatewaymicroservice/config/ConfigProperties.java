package com.wexinc.wexgatewaymicroservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties
public class ConfigProperties {
    private String ifcsservice;
    private String clientservice;


}
