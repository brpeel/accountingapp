package org.spsu.accounting.report.resource

import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dbi.AccountStatementDBI
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.report.data.IncomeStatement

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.sql.Timestamp

/**
 * Created by bpeel on 4/11/15.
 */
@Path("report")
class IncomeStatementResource {
    static final Logger logger =  LoggerFactory.getLogger(IncomeStatementResource)

    AccountStatementDBI dbi

    @GET
    @Path("/income/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public IncomeStatement getStatement(@PathParam("year") int year, @PathParam("month") int month){

        logger.info("getting income statement for $year $month")
        IncomeStatement statement = new IncomeStatement()

        List<AccountStatement> accounts = getAccounts(2015, 4, "'Revenue', 'Expense'")

        statement.addAccounts(accounts)

        return statement
    }

    private List<AccountStatement> getAccounts(int year, int month, String types){
        DateTime start = new DateTime(year, month, 1, 0, 0)
        DateTime end = start.plusMonths(1)

        return dbi.getBalances(new Timestamp(start.millis), new Timestamp(end.millis), types, "<")
    }
}
