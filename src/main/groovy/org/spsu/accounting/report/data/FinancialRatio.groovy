package org.spsu.accounting.report.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.spsu.accounting.data.domain.CategoryTotal
import org.spsu.accounting.data.serial.MoneySerializer
import org.spsu.accounting.data.serial.MoneyWithSignSerializer
import org.spsu.accounting.data.serial.PercentageSerializer
import org.spsu.accounting.data.serial.RatioSerializer

/**
 * Created by bpeel on 4/28/15.
 */
class FinancialRatio extends Statement{

    @JsonSerialize(using = RatioSerializer.class)
    BigDecimal currentRatio = 0.0                      // Current assets / Current liabilities

    @JsonSerialize(using = RatioSerializer.class)
    BigDecimal quickRatio = 0.0                        //(Current assets-Inventory) / Current Liabilities

    @JsonSerialize(using = RatioSerializer.class)
    BigDecimal inventoryToNetWorkingCapital = 0.0      //Inventory /(Current assets - Current Liabilities)

    @JsonSerialize(using = PercentageSerializer.class)
    BigDecimal debtToAssetRatio =  0.0                 //Total debt / Total assets

    @JsonSerialize(using = PercentageSerializer.class)
    BigDecimal debtToEquityRatio = 0.0                 //Total debt / Total stockholders' equity

    @JsonSerialize(using = PercentageSerializer.class)
    BigDecimal longTermDebtToEquityRatio = 0.0         //Long-term debt / Total shareholders' equity

    @JsonSerialize(using = MoneyWithSignSerializer.class)
    BigDecimal currentAssets = 0.0

    @JsonSerialize(using = MoneyWithSignSerializer.class)
    BigDecimal inventory = 0.0

    @JsonSerialize(using = MoneyWithSignSerializer.class)
    BigDecimal totalAssets = 0.0

    @JsonSerialize(using = MoneyWithSignSerializer.class)
    BigDecimal currentLiabilities = 0.0

    @JsonSerialize(using = MoneyWithSignSerializer.class)
    BigDecimal totalLiabilities = 0.0

    @JsonSerialize(using = MoneyWithSignSerializer.class)
    BigDecimal longTermLiabilities = 0.0

    @JsonSerialize(using = MoneyWithSignSerializer.class)
    BigDecimal stakeHolderEquity = 0.0

    private BigDecimal ownerEquity = 0.0
    private BigDecimal income = 0.0

    public void computeValues(List<CategoryTotal> totals){

        totals?.each { CategoryTotal total ->
            String category = total.category.toLowerCase().replace(" ", "")

            switch (category){
                case "ownerequity" : setEquity(total); break;
                case "expense"     : setExpense(total); break;
                case "asset"       : setAssets(total); break;
                case "revenue"     : setRevenue(total); break;
                case "liability"   : setLiability(total); break;
            }
        }

        stakeHolderEquity = ownerEquity + income
        longTermLiabilities = totalLiabilities - currentLiabilities

        currentRatio = currentAssets / currentLiabilities
        quickRatio = (currentAssets-inventory) / currentLiabilities
        inventoryToNetWorkingCapital = inventory /(currentAssets - currentLiabilities)
        debtToAssetRatio =  totalLiabilities / totalAssets
        debtToEquityRatio = totalLiabilities / stakeHolderEquity
        longTermDebtToEquityRatio = totalLiabilities / stakeHolderEquity
    }

    private void setEquity(CategoryTotal total){
        if (total.subcategory == "Investment")
            ownerEquity += total.balance
        else if (total.subcategory == "Withdraw")
            ownerEquity -= total.balance
    }

    private void setAssets(CategoryTotal total){
        if (total.subcategory == "Intangible Asset")
            return;

        totalAssets += total.balance
        if (total.subcategory == "Current Asset")
            currentAssets += total.balance

    }

    private void setExpense(CategoryTotal total){
        income -= total.balance
    }

    private void setRevenue(CategoryTotal total){
        income += total.balance
    }

    private void setLiability(CategoryTotal total){
        totalLiabilities += total.balance
        if (total.subcategory in ["Current Liability", "Cash", "Inventory"])
            currentLiabilities += total.balance
        if (total.subcategory == "Inventory")
            inventory += total.balance
    }


    private BigDecimal getLongTermAsset(){
        return totalAssets.subtract(currentAssets)
    }

    private BigDecimal getLongTermLiability(){
        return
    }

}
