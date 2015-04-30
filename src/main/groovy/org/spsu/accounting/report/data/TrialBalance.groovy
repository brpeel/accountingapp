package org.spsu.accounting.report.data

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import org.spsu.accounting.data.domain.AccountStatement
import org.spsu.accounting.data.serial.MoneySerializer

/**
 * Created by bpeel on 4/29/15.
 */
class TrialBalance {

    final List<TrialBalanceAccount> accounts = []
    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal totalCredit = 0.0

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal totalDebit = 0.0

    @JsonGetter("date")
    public String getDate(){
        DateTime now = new DateTime();

        String month = new java.text.DateFormatSymbols().months[ now.monthOfYear + 1 ]

        return "${month} ${now.dayOfMonth}, ${now.year}"

    }

    void addAccounts(List<AccountStatement> accounts){

        if (accounts == null)
            return;

        boolean foundCredit = false
        boolean foundDedit = false
        accounts.each{ AccountStatement account ->
            TrialBalanceAccount tbAccount = new TrialBalanceAccount(id: account.id, name: account.name)

            if (account.debitNormal) {
                tbAccount.debit = convertBalance(account.balance, !foundDedit)
                foundDedit = true
                totalDebit += account.balance
            }
            else {
                tbAccount.credit = convertBalance(account.balance, !foundCredit)
                foundCredit = true
                totalCredit += account.balance
            }
            this.accounts << tbAccount
        }
    }

    private String convertBalance(BigDecimal balance, boolean isFirst){
        String value = String.format("%,.2f", balance.setScale(2, BigDecimal.ROUND_HALF_UP))
        if (isFirst)
            return "\$$value"
        return value
    }

    private class TrialBalanceAccount{
        int id

        String name

        String credit = null

        String debit = null

        boolean isFirst = false;
    }

}
