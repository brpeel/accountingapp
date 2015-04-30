package org.spsu.accounting.report.resource

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dbi.AccountStatementDBI
import org.spsu.accounting.data.dbi.CategoryTotalDBI
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.data.domain.CategoryTotal
import org.spsu.accounting.report.data.Period

/**
 * Created by brettpeel on 4/12/15.
 */
abstract class ReportResource {


    Logger logger = LoggerFactory.getLogger(getClass())

    AccountStatementDBI accountDBI
    CategoryTotalDBI categoryTotalDBI

    abstract def getStatement(int year, int month)

    protected List<AccountStatement> getAccounts(int year, int month, String types) {

        Period p = getPeriod(year, month)
        //return dbi.getBalances(p.startTime, p.endTime, types, "<")
        return accountDBI.getBalances(types)
    }

    protected Period getPeriod(int year, int month) {
        return new Period(year, month)
    }

    protected List<CategoryTotal> getCategoryTotals(int year, int month) {
        return categoryTotalDBI.getTotals()
    }


    protected List<CategoryTotal> getSubcategoryTotals(int year, int month) {
        return categoryTotalDBI.getTotalsBySubCategory()
    }
}
