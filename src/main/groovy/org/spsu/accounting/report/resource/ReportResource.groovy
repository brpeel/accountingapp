package org.spsu.accounting.report.resource

import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dbi.AccountStatementDBI
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.report.data.Period

import java.sql.Timestamp

/**
 * Created by brettpeel on 4/12/15.
 */
abstract class ReportResource {


    Logger logger =  LoggerFactory.getLogger(getClass())

    AccountStatementDBI dbi

    protected List<AccountStatement> getAccounts(int year, int month, String types){

        Period p = getPeriod(year, month)
        return dbi.getBalances(p.startTime, p.endTime, types, "<")
    }

    protected Period getPeriod(int year, int month){
        return new Period(year, month)
    }

}
