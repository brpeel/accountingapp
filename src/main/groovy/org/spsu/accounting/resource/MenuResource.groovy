package org.spsu.accounting.resource

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.PermissionSet
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.data.domain.UserRole

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by brettpeel on 2/7/15.
 */
@Path("api/menu")
class MenuResource {

    static Logger logger = LoggerFactory.getLogger(AuthResource)

    private UserDAO dao;

    @GET
    @Path("actions")
    @Produces(MediaType.APPLICATION_JSON)
    public getActions(@Context HttpServletRequest request){

        int userid = request.getAttribute("userid")

        UserDO user = dao.get(userid)

        UserRole maxRole = user.maxRole()
        Set permissions = PermissionSet.getPermissions(maxRole)
        Set mainMenu = PermissionSet.getPermissionsByGroup(maxRole)."MainMenu"

        return Response.ok().entity(["menuItems":mainMenu, "permissions":permissions, "username":request.getAttribute("username")]).build()
    }

}
