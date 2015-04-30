package org.spsu.accounting.report.resource

import com.sun.jersey.api.client.ClientResponse
import org.spsu.accounting.report.data.TrialBalance

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType

/**
 * Created by bpeel on 4/29/15.
 */
@Path("report/trailbalance")
class TrailBalanceResource extends ReportResource {

    @GET
    @Path("/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    TrialBalance getStatement(int year, int month) {
        throw new WebApplicationException(ClientResponse.Status.NOT_IMPLEMENTED);
    }
}
