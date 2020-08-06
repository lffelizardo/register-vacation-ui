package br.com.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebConfig {

    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Bean
    public Filter noCacheFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                // No init needed
            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                final HttpServletResponse res = (HttpServletResponse) servletResponse;
                res.setHeader("Cache-Control", "no-cache");
                filterChain.doFilter(servletRequest, servletResponse);
            }

            @Override
            public void destroy() {
                // No destroy method needed
            }
        };
    }

}