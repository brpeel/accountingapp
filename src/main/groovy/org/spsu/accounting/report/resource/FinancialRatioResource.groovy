package org.spsu.accounting.report.resource

import org.spsu.accounting.report.data.FinancialRatio

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Created by bpeel on 4/29/15.
 */
@Path("report/financialratio")
class FinancialRatioResource extends ReportResource{

    @GET
    @Path("/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    FinancialRatio getStatement(@PathParam("year") int year, @PathParam("month") int month){
        FinancialRatio ratio = new FinancialRatio()
        ratio.computeValues(getSubcategoryTotals(year, month))

        return ratio
    }
}
