package org.spsu.accounting.auth

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.UserDAO

import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Request
import javax.ws.rs.core.Response

/**
 * Created by brettpeel on 2/8/15.
 */
class SessionFilter implements javax.servlet.Filter{

    Logger logger = LoggerFactory.getLogger(SessionFilter)

    UserDAO dao;

    @Override
    void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest

        String token = request.getSession().getAttribute("token")
        if (request.getRequestURI().endsWith("authenticate") || dao.isValidSession(token)) {

            chain.doFilter(servletRequest, response)
            return
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendRedirect("http://localhost:8080/auth/authenticate")
    }

    @Override
    void destroy() {

    }
}
