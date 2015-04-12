package org.spsu.accounting.report.resource

import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dbi.AccountStatementDBI
import org.spsu.accounting.data.domain.AccountStatement

import java.sql.Timestamp

/**
 * Created by brettpeel on 4/12/15.
 */
abstract class ReportResource {


    Logger logger =  LoggerFactory.getLogger(getClass())

    AccountStatementDBI dbi

    private List<AccountStatement> getAccounts(int year, int month, String types){
        DateTime start = new DateTime(year, month, 1, 0, 0)
        DateTime end = start.plusMonths(1)

        return dbi.getBalances(new Timestamp(start.millis), new Timestamp(end.millis), types, "<")
    }
}
