package org.spsu.accounting.report.resource

import org.spsu.accounting.report.data.IncomeStatement

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Created by brettpeel on 4/12/15.
 */
class OwnerEquityResource extends ReportResource {

    @GET
    @Path("/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public IncomeStatement getStatement(@PathParam("year") int year, @PathParam("month") int month) {


    }
}