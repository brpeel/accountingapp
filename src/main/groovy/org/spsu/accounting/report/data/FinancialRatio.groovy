package org.spsu.accounting.report.data

/**
 * Created by bpeel on 4/28/15.
 */
class FinancialRatio {
    /*
    *
    *
    Current ratio = current assets / current liabilities

    Quick ratio  = (cash + marketable securities + net receivables) / current liabilities

    Debt-to-asset ratio = total liabilities / total assets

    Debt-equity ratio = long-term debt / shareholder's equity

    ROA = net income / total average assets

    ROE = net income / total stockholders equity

    * */

    BigDecimal netIncome
    BigDecimal ownerEquity
    BigDecimal longTermDebt
    BigDecimal totalLiabilities
    BigDecimal totalAssets
    BigDecimal cash
    BigDecimal securities
    BigDecimal accountsReveivable
    BigDecimal currentAssets
    BigDecimal currentliabilities

 }
