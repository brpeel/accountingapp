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

    private class MenuItem {
        @JsonProperty("label")
        String label

        @JsonProperty("action")
        String action

        @JsonProperty("icon")
        String icon

        public MenuItem(String label, String action, String icon){
            this.label = label
            this.action = action
            this.icon = icon
        }
    }

    static Logger logger = LoggerFactory.getLogger(AuthResource)

    private final UserDAO dao;

    public static final Set<MenuItem> UserActions = []
    public static final Set<MenuItem> ManagerActions = []
    public static final Set<MenuItem> AdminActions = []

    @GET
    @Path("actions")
    @Produces(MediaType.APPLICATION_JSON)
    public getActions(@Context HttpServletRequest request){

        int userid = request.getAttribute("userid")

        UserDO user = request.getAttribute("user")

        UserRole maxRole = user.maxRole()
        Set permissions = PermissionSet.getPermissions(maxRole)
        Set mainMenu = PermissionSet.getPermissionsByGroup(maxRole)."MainMenu"

        return Response.ok().entity(["menuItems":mainMenu, "permissions":permissions, "username":request.getAttribute("username")]).build()
    }


    protected Map<String, List<MenuItem>> getAllowedMenuItems(UserDO user){

    }
}
