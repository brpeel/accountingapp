package org.spsu.accounting.resource

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.auth.Auth
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate( @Context HttpServletRequest request, Map req) {
        UserDO user = dao.checkLogin(req.username, req.password)
        if (!user)
            return Response.status(Response.Status.UNAUTHORIZED).build()

        String token =  dao.createSession(user);
        MenuItem[] items = [new MenuItem("Users", "user"), new MenuItem("Accounts", "accounts"), new MenuItem("Transactions", "transactions"), new MenuItem("Reports", "reports")]
        Map data = ["token":token, "menuItems":items]

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
