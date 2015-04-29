package org.spsu.accounting.report.data

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.data.serial.MoneySerializer

/**
 * Created by bpeel on 4/11/15.
 */
class IncomeStatement {

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal totalRevenues = 0.0

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal totalExpenses = 0.0

    List<AccountStatement> revenues = []
    List<AccountStatement> expenses = []

    public void addAccounts(List<AccountStatement>  accounts){
        accounts?.each {AccountStatement account->
            account.name = "${account.name}"
            if (account.category.toLowerCase() == "revenue"){
                revenues.add(account)
                totalRevenues += account.balance
            }
            else if (account.category.toLowerCase() == "expense"){
                expenses.add(account)
                totalExpenses += account.balance
            }
        }
        //revenues.add(new AccountStatement(name:"Total Revenues", balance: totalRevenues))
        //expenses.add(new AccountStatement(name:"Total Expenses", balance: totalExpenses))
    }

    @JsonGetter("netIncome")
    @JsonSerialize(using = MoneySerializer.class)
    public BigDecimal netIncome(){
        return totalRevenues.subtract(totalExpenses)
    }
}
