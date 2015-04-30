package org.spsu.accounting.report.resource

import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.report.data.BalanceSheet
import org.spsu.accounting.report.data.OwnerEquity

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Created by bpeel on 4/21/15.
 */
@Path("report/balancesheet")
class BalanceSheetResource extends ReportResource {

    @GET
    @Path("/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public BalanceSheet getStatement(@PathParam("year") int year, @PathParam("month") int month) {

        BalanceSheet statement = new BalanceSheet()

        List<AccountStatement> accStmt = getAccounts(year, month)
        statement.addAccounts(accStmt)

        return statement
    }

}
