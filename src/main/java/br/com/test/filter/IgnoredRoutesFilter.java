package br.com.test.filter;

import br.com.test.exception.ZuulFilterException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IgnoredRoutesFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(IgnoredRoutesFilter.class);

    @Value("${zuul.filter.ignored.routes}")
    private String ignoreUrlPatterns;

    @Value("${zuul.filter.ignored.routes.enabled}")
    private Boolean ignoredRoutesFilterEnabled;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        if (ignoredRoutesFilterEnabled) {
            String requestUri = RequestContext.getCurrentContext().getRequest().getRequestURI();
            for (String pattern: ignoreUrlPatterns.split(",")) {
                if (pathMatcher.match(pattern.trim(), requestUri)) {
                    logger.info("Ignore URL filter activated for " + requestUri);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object run() {
        String requestUri = RequestContext.getCurrentContext().getRequest().getRequestURI();
        logger.info("Blocking execution of: " + requestUri);
        HttpServletResponse response = RequestContext.getCurrentContext().getResponse();
        try {
            response.sendError(404, requestUri + " cannot be requested. The URL was blocked by " + this.getClass().getSimpleName());
            return null;
        } catch (IOException e) {
            throw new ZuulFilterException(e);
        }
    }

}