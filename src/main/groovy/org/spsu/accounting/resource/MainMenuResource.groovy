package org.spsu.accounting.resource

import com.fasterxml.jackson.annotation.JsonProperty

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by brettpeel on 2/7/15.
 */
@Path("api/mainmenu")
class MainMenuResource {

    @GET
    @Path("actions")
    @Produces(MediaType.APPLICATION_JSON)
    public getActions(){

        MenuItem[] items = [new MenuItem("Users", "user"), new MenuItem("Accounts", "account"), new MenuItem("Transactions", "transaction"), new MenuItem("Reports", "report")]
        return Response.ok().entity(items).build()
    }

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
}
