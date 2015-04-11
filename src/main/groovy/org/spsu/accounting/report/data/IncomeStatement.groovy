package org.spsu.accounting.report.data

/**
 * Created by bpeel on 4/11/15.
 */
class IncomeStatement {
    public static class Account{
        String name
        BigDecimal balance
    }

    List<Account> revenues = []
    List<Account> expenses = []

    public BigDecimal totalRevenues(){
        return sum(revenues)
    }

    public BigDecimal totalExpenses(){
       return sum(expenses)
    }

    private BigDecimal sum(List<Account> accounts){
        BigDecimal total = BigDecimal.ZERO

        accounts?.each{ Account i ->
            total += i.balance
        }
        return total
    }
}
