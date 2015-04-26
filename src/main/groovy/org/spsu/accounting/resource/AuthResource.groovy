package org.spsu.accounting.resource

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO

import javax.servlet.http.HttpServletRequest
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


    private class MenuItem {
        @JsonProperty("label")
        String label

        @JsonProperty("action")
        String action

        public MenuItem(String label, String action){
            this.label = label
            this.action = action
        }
    }

    static Logger logger = LoggerFactory.getLogger(AuthResource)

    private final UserDAO dao;

    public AuthResource(UserDAO dao){
        this.dao = dao
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpServletRequest request){
        String token = request.getHeader("Authorization")
        dao.clearSession(token)
        return Response.ok().build()
    }

    @POST
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(Map req) {
        UserDO user = dao.checkLogin(req.username, req.password)
        if (!user)
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username and/or password. Please verify your logon credentials.").build()

        if (user.loginAttempts >= 3)
            return Response.status(Response.Status.UNAUTHORIZED).entity("Your account has been locked. Please Contact your administrator.").build()

        boolean passwordExpired = dao.isPasswordExpired(user)
        boolean resetOnLogon = user.resetOnLogon

        String token =  dao.createSession(user);


        Map data = ["token":token, "username":user.username, "userid":user.id, "reset_on_logon":resetOnLogon, "password_expired":passwordExpired]

        return Response.ok(data).build();
    }

    @POST
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset( @Context HttpServletRequest request, Map req) {
        try {
            UserDO user = dao.get(req.username)
            if (!user)
                return Response.status(Response.Status.UNAUTHORIZED).build()

            dao.resetPassword(user)

            return Response.ok(user).build();
        }
        catch (Exception e){
            logger.error("Error resetting user password", e)
            return Response.serverError().build()
        }
    }

}
