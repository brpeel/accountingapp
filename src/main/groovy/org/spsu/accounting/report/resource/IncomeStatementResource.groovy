package org.spsu.accounting.report.resource

import org.spsu.accounting.data.dbi.AccountStatementDBI
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.report.data.IncomeStatement
import org.spsu.accounting.report.view.IncomeStatementView

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Created by bpeel on 4/11/15.
 */
@Path("/report/incomestatement")
@Produces(MediaType.TEXT_HTML)
class IncomeStatementResource {
    AccountStatementDBI dbi

    @Path("/{year}/{month}")
    @GET
    public IncomeStatementView incomeStatement(@PathParam("id") int year, @PathParam("month") int month){

        IncomeStatement statement = new IncomeStatement()
        List<AccountStatement> accounts = dbi.getRevenuesAndExpenses(year, month, "'Revenue', 'Expense'")
    }
}
