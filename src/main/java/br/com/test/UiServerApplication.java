package br.com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SpringBootApplication
@ComponentScan(basePackages="br.com.test")
@EnableZuulProxy
public class UiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UiServerApplication.class, args);
	}

	@Qualifier("jwtTokenEnhancer")
	@Autowired
	JwtAccessTokenConverter jwtAccessTokenConverter;

	@Bean
	@Qualifier("tokenStore")
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter);
	}

	@Bean
	public JwtAccessTokenConverter jwtTokenEnhancer(@Value("${register.public.key}") String publicKey) {
		JwtAccessTokenConverter converter =  new JwtAccessTokenConverter();
		converter.setVerifierKey(publicKey);
		return converter;
	}

}