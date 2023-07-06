package com.jnetdata.msp.core.filter;

import com.jnetdata.msp.core.context.ThreadLocalContextUtil;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Administrator
 */
public class ActionContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        }finally {
            ThreadLocalContextUtil.clearContext();
        }
    }

    @Override
    public void destroy() {

    }

}
