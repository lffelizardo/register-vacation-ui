package br.com.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableOAuth2Client
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    TokenStore tokenStore;

    public static final String RESOURCE_ID = "restService";

    @Bean
    @Qualifier("resource")
    public OAuth2ProtectedResourceDetails resource(){
        OAuth2ProtectedResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        return resourceDetails;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String contentSecurityPolicy = new StringBuilder()
            .append("default-src 'self' *.amazonaws.com *.testnav.com https://accounts.google.com www.googletagmanager.com www.google-analytics.com; ")
            .append("style-src * 'unsafe-inline'; ")
            .append("script-src 'self' 'unsafe-inline' *.testnav.com https://accounts.google.com https://ajax.googleapis.com https://apis.google.com www.googletagmanager.com www.google-analytics.com; ")
            .append("font-src *; ")
            .append("img-src *; ")
            .append("connect-src *")
            .toString();
        http.headers()
                .frameOptions().disable()
                .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy",contentSecurityPolicy));

        // ...
        http.anonymous().and().authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/api/**").authenticated();
    }

}
