package org.spsu.accounting.report.data

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.spsu.accounting.data.dao.AccountDAO
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.data.serial.MoneySerializer

/**
 * Created by bpeel on 4/14/15.
 */
class BalanceSheet extends Statement {

    final List<AccountStatement> assets = new ArrayList<>()
    final List<AccountStatement> liabilities = new ArrayList<>()

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal investment = 0.0;

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal drawing = 0.0;

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal expenses = 0.0;

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal revenues = 0.0;

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal totalAssets = 0.0;

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal totalLiabilities = 0.0;

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal ownerEquity = 0.0;

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal ownersEquityPlusLiabilities = 0.0;


    public void addAccounts(List<AccountStatement> accounts){
        accounts?.each {AccountStatement account->
            String category = account.category.toLowerCase().replace(" ","")
            String subcategory = account.subcategory?.toLowerCase()?.replace(" ","")

            if (category == "asset") {
                assets << account
                totalAssets += account.balance
            }
            else if (category == "liability") {
                liabilities << account
                totalLiabilities += account.balance
            }
            else if (category == "ownerequity") {
                if (subcategory == "investment")
                    investment += account.balance
                if (subcategory == "withdraw")
                    drawing += account.balance
            }
            else if (category == "expense"){
                expenses += account.balance
            }
            else if (category == "revenue"){
                revenues += account.balance
            }
        }

        ownerEquity = (revenues - expenses) + (investment - drawing)
        ownersEquityPlusLiabilities = ownerEquity + totalLiabilities
    }
}
