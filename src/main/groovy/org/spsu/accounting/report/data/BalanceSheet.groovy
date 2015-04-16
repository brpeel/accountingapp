package org.spsu.accounting.report.data

import org.spsu.accounting.data.dao.AccountDAO
import org.spsu.accounting.data.domain.AccountStatement

/**
 * Created by bpeel on 4/14/15.
 */
class BalanceSheet {

    final Map<String, List<AccountStatement>> assets = new HashMap<>()
    final Map<String, List<AccountStatement>> liabilities = new HashMap<>()
    final Map<String, List<AccountStatement>> ownerEquity = new HashMap<>()

    public void addAccounts(List<AccountStatement> accounts){
        accounts?.each {AccountStatement account->
            String category = account.category.toLowerCase().replace(" ","")
            Map collection = null
            if (category == "asset")
                collection = assets
            else if (category == "liability")
                collection = liabilities
            else if (category == "ownerequity")
                collection = ownerEquity

            addAccount(collection, account)
        }
    }

    private void addAccount(Map<String, List<AccountStatement>> collection, AccountStatement account){
        String subcategory = account.subcategory ?: "other"

        List<AccountStatement> accounts = collection."$subcategory"
        if (!accounts) {
            accounts = new ArrayList<AccountStatement>()
            collection."$subcategory" = accounts
        }
        accounts.add(account)
    }
}
