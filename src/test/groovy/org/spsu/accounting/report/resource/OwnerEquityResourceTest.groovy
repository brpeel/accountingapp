package org.spsu.accounting.report.resource

import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dao.AccountDAO
import org.spsu.accounting.data.dao.impl.AccountDAOImpl
import org.spsu.accounting.data.dbi.AccountDBI
import org.spsu.accounting.data.dbi.AccountStatementDBI
import org.spsu.accounting.report.data.OwnerEquity
import spock.lang.Specification

/**
 * Created by bpeel on 4/13/15.
 */
class OwnerEquityResourceTest extends Specification {

    static AccountStatementDBI dbi
    static AccountDAO accountDAO
    static OwnerEquityResource resource

    void setupSpec()
    {
       DBConnection db = DBConnection.openConnection("OwnerEquityResource")
       dbi = db.onDemand(AccountStatementDBI)

       accountDAO = new AccountDAOImpl(dbi: db.onDemand(AccountDBI))
       resource = new OwnerEquityResource(accountDBI: dbi, accountDAO: accountDAO)
    }

    def "GetStatement"() {

        when:
        OwnerEquity result = resource.getStatement(2015, 04)

        then:
        result.startingBalance == 0
        result.incomePlusInvestments() == 23965.00
        result.netIncome == 3965.00
        result.endingBalance() == 22865.00
        result.accountName == "George Fielding, Capital"
        //result.startDate == "April 01, 2015"
        //result.endDate == "April 30, 2015"

    }
}
