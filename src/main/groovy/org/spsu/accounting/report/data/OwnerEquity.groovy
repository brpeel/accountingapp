package org.spsu.accounting.report.data

import org.spsu.accounting.data.domain.AccountStatement

/**
 * Created by brettpeel on 4/12/15.
 */
class OwnerEquity {

    BigDecimal startingBalance

    BigDecimal investments

    BigDecimal withdrawals

    BigDecimal  endingBalance

    public void addAccounts(List<AccountStatement>  accounts){
        accounts?.each {AccountStatement account->
            if (account.category.toLowerCase() == "investment"){
                investments += account.balance
            }
            else if (account.category.toLowerCase() == "withdraw"){
                withdrawals += account.balance
            }
        }
    }
}
