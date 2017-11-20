package com.hybris.caas.oauth.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.hybris.caas.log", "com.hybris.caas.multitenancy", "com.hybris.caas.oauth.client"})
public class OauthClientApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OauthClientApplication.class, args);
	}
}
