package com.wexinc.wexgatewaymicroservice;

import com.wexinc.wexgatewaymicroservice.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(value= ConfigProperties.class)
public class WexGatewayMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WexGatewayMicroserviceApplication.class, args);
	}
	@Bean
	@LoadBalanced

	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Do any additional configuration here
		return builder.build();
	}
}
