package org.spsu.accounting.resource

import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.resource.base.BaseResource

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 1/28/15.
 */

@Path("/about")
class AboutResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbout() {
        Map app = ["name":"Accounting Application", "version":"1.0.0", "author":"Brett Peel", "contact":"bpeel56@gmail.com"]
        return Response.ok().entity(app).build()
    }
}
