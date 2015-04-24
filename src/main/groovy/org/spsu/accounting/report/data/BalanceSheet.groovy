package org.spsu.accounting.report.data

import org.spsu.accounting.data.dao.AccountDAO
import org.spsu.accounting.data.domain.AccountStatement

/**
 * Created by bpeel on 4/14/15.
 */
class BalanceSheet {

    final List<AccountStatement> assets = new ArrayList<>()
    final List<AccountStatement> liabilities = new ArrayList<>()
    final List<AccountStatement> ownerEquity = new ArrayList<>()

    public void addAccounts(List<AccountStatement> accounts){
        accounts?.each {AccountStatement account->
            String category = account.category.toLowerCase().replace(" ","")
            if (category == "asset")
                assets << account
            else if (category == "liability")
                liabilities << account
            else if (category == "ownerequity")
                ownerEquity << account

        }
    }

}
