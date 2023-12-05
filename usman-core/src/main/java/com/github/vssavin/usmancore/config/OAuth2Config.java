package com.github.vssavin.usmancore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for Google O2Auth.
 *
 * @author vssavin on 29.11.2023
 */
@PropertySource(value = "classpath:" + OAuth2Config.CONFIG_FILE, ignoreResourceNotFound = true)
@Configuration
public class OAuth2Config {

	static final String CONFIG_FILE = "oauth2.properties";

	@Value("${spring.security.oauth2.client.registration.google.clientId:}")
	private String googleClientId;

	@Value("${spring.security.oauth2.client.registration.google.clientSecret:}")
	private String googleClientSecret;

	public String getGoogleClientId() {
		return googleClientId;
	}

	public String getGoogleClientSecret() {
		return googleClientSecret;
	}

}
