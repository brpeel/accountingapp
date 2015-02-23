package org.spsu.accounting.resource

import io.dropwizard.auth.Auth
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.utils.AuthUtils

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
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

    @GET
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(UserDO user, @Context HttpServletRequest request) {

        request.getSession().setAttribute("token", dao.createSession(user))
        return Response.temporaryRedirect("/ui");
    }

    @POST
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset(@PathParam("username") String username){
        UserDO user = dao.get(username)

        this.dao.resetPassword(user)

        return Response.temporaryRedirect("/ui/login")
    }

}
