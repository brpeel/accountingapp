package org.spsu.accounting.resource

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.PermissionSet
import org.spsu.accounting.data.domain.UserDO

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

        def userid = request.getAttribute("userid")
        Map data = determinePermissions(userid)
        data."username" = request.getAttribute("username")

        return Response.ok().entity(data).build()
    }

    private Map determinePermissions(int userid){
        UserDO user = dao.get(userid)

        int role = user.role
        Set permissions = PermissionSet.getPermissions(role)
        Set mainMenu = PermissionSet.getPermissionsByGroup(role)."MainMenu"

        return ["menuItems":mainMenu, "permissions":permissions]
    }
}
