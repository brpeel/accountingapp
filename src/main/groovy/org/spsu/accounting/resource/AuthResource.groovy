package org.spsu.accounting.resource

import io.dropwizard.auth.Auth
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by brettpeel on 2/8/15.
 */
@Path("auth")
class AuthResource {

    private final UserDAO dao;

    public AuthResource(UserDAO dao){
        this.dao = dao
    }

    @POST
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDO authenticate( @Context HttpServletRequest request, Map req) {
        UserDO user = dao.checkLogin(req.username, req.password)

        request.getSession().setAttribute("token", dao.createSession(user))
        return user;
    }

}
