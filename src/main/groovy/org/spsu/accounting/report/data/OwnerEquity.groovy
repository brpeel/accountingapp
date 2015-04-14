package org.spsu.accounting.report.data

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.data.serial.MoneySerializer

import java.text.SimpleDateFormat

/**
 * Created by brettpeel on 4/12/15.
 */
class OwnerEquity {

    final static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy")

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal startingBalance = BigDecimal.ZERO

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal investments = BigDecimal.ZERO

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal withdrawals = BigDecimal.ZERO

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal netIncome = BigDecimal.ZERO

    String accountName
    String startDate
    String endDate

    public void addAccounts(List<AccountStatement> accounts) {
        accounts?.each { AccountStatement account ->
            String category = account.category.toLowerCase()
            String subcat = account.subcategory?.toLowerCase()
            if (category == "owner equity") {
                if (subcat == "investment")
                    investments += account.balance ?: BigDecimal.ZERO
                else if (subcat == "withdraw")
                    withdrawals += account.balance ?: BigDecimal.ZERO
            } else if (category == "revenue") {
                netIncome += account.balance ?: BigDecimal.ZERO
            } else if (category == "expense") {
                netIncome -= account.balance ?: BigDecimal.ZERO
            }
        }

    }

    @JsonGetter("incomePlusInvestments")
    @JsonSerialize(using = MoneySerializer.class)
    public BigDecimal incomePlusInvestments() {
        return investments + netIncome
    }

    @JsonGetter("endingBalance")
    @JsonSerialize(using = MoneySerializer.class)
    public BigDecimal endingBalance() {
        return incomePlusInvestments() - withdrawals
    }

    public void setAccount(AccountDO accountDO) {
        startingBalance = accountDO.initialBalance
        accountName = accountDO.name
    }

    public void setPeriod(Period period) {

        startDate = dateFormat.format(new Date(period.start.millis))
        DateTime inclusiveEnd = period.end.minusDays(1)
        endDate = dateFormat.format(new Date(inclusiveEnd.millis))
    }
}
