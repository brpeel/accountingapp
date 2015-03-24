package org.spsu.accounting.resource

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 1/28/15.
 */

@Path("open/about")
class AboutResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbout() {
        Map app = ["name":"Accounting Application", "version":"1.0.0", "author":"Brett Peel", "contact":"bpeel56@gmail.com"]
        return Response.ok().entity(app).build()
    }
}
