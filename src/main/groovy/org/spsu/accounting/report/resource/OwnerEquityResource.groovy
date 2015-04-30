package org.spsu.accounting.report.resource

import org.spsu.accounting.data.dao.AccountDAO
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.report.data.IncomeStatement
import org.spsu.accounting.report.data.OwnerEquity
import org.spsu.accounting.report.data.Period

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Created by brettpeel on 4/12/15.
 */
@Path("report/ownerequity")
class OwnerEquityResource extends ReportResource {

    AccountDAO accountDAO

    @GET
    @Path("/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public OwnerEquity getStatement(@PathParam("year") int year, @PathParam("month") int month) {

        OwnerEquity statement = new OwnerEquity()

        List<AccountStatement> accStmt = getAccounts(year, month)//getAccounts(year, month, "'Revenue', 'Expense', 'Owner Equity'")
        statement.addAccounts(accStmt)

        Period period = getPeriod(year, month)
        statement.account = accountDAO.getAccountByType(period, 'Owner Equity', 'Investment')?.get(0)
        //statement.period = period

        return statement
    }
}