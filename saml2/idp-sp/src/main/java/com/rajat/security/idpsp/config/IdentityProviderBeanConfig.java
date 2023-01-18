package com.rajat.security.idpsp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.saml.provider.SamlServerConfiguration;
import org.springframework.security.saml.provider.identity.config.SamlIdentityProviderServerBeanConfiguration;

@Configuration
public class IdentityProviderBeanConfig extends SamlIdentityProviderServerBeanConfiguration {

	private final AppConfig appConfig;

	public IdentityProviderBeanConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	@Override
	protected SamlServerConfiguration getDefaultHostSamlServerConfiguration() {
		return appConfig;
	}
}
