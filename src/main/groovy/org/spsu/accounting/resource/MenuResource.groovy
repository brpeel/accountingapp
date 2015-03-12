package org.spsu.accounting.resource

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.UserDAO
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

    private enum UserType {
        user,
        manager,
        admin

        public static UserType getUserType(String type){
            type = type?.toLowerCase()

            UserType userType = user
            if (type == "manager")
                userType = manager
            if (type == "admin")
                userType = admin
            return userType
        }
    }

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

    @GET
    @Path("actions")
    @Produces(MediaType.APPLICATION_JSON)
    public getActions(@Context HttpServletRequest request){

        int userid = request.getAttribute("userid")
        dao.
        MenuItem[] items = [new MenuItem("Users", "user"), new MenuItem("Accounts", "account"), new MenuItem("Transactions", "transaction"), new MenuItem("Reports", "report")]
        return Response.ok().entity(["menuItems":items, "username":request.getAttribute("username")]).build()
    }


    private Map<String, List<MenuItem>> getAllowedMenuItems(){

    }
}
