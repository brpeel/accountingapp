package org.spsu.accounting.auth

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
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

        String path = request.getRequestURI()

        logger.info("Checking path : ${path}")
        if (!isOpenPath(path)){
            String token = request.getHeader("Authorization")
            if (!token)
                token = request.getParameter("Authorization")
            UserDO user = isValid(token)
            if (!user){
                ((HttpServletResponse) response).setStatus(Response.Status.UNAUTHORIZED.statusCode)
                return
            }
            request.setAttribute("userid", user.id)
            request.setAttribute("username", user.username)
            request.setAttribute("user", user)
        }

        chain.doFilter(servletRequest, response)
    }

    private UserDO isValid(String token){
        try {
            return dao.isValidSession(token)
        }
        catch (Exception e){
            logger.info("Could not check session status", e)
        }
        return null
    }

    private isOpenPath(String path){
       // return true

        if (path.startsWith("/report/"))
            return true
        //if (path.startsWith("/api/transdocument/download/"))
            //return true
        return path == "/ui" || path.startsWith("/ui/") || path.startsWith("/open/") || path.startsWith("/auth/")
    }

    @Override
    void destroy() {

    }
}
