package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime

public class AccountDO extends ActiveBaseDO{

	@JsonProperty("name")
	String name

	@JsonProperty("initial_balance")
	Float initialBalance

	@JsonProperty("normal_side")
	String normalSide

	@JsonProperty("added")
	DateTime added

	@JsonProperty("added_by")
	int addedBy

	@JsonProperty("subcategory")
	String subcategory

    public void setInitialBalance(String amount){
        this.initialBalance = Float.parseFloat(amount)
    }
}
