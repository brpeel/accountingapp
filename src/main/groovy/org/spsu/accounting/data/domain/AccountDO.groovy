package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.dropwizard.validation.ValidationMethod
import org.joda.time.DateTime
import org.spsu.accounting.data.serial.DateTimeSerializer
import org.spsu.accounting.data.serial.MoneySerializer

import javax.validation.constraints.NotNull

public class AccountDO extends ActiveBaseDO{

	@JsonProperty("name")
    @NotNull
	String name

	@JsonProperty("initial_balance")
    @NotNull
    @JsonSerialize(using = MoneySerializer.class)
	BigDecimal initialBalance = 0.0

	@JsonProperty("normal_side")
    @NotNull
	String normalSide

	@JsonProperty("added")
    @JsonSerialize(using = DateTimeSerializer)
	DateTime added

	@JsonProperty("added_by")
    @NotNull
	int addedBy

    @JsonProperty("category")
    String category

    @JsonProperty("subcategory")
	String subcategory

    public void setInitialBalance(String amount){
        this.initialBalance = new BigDecimal(amount)
    }

    @ValidationMethod(message="normal side must be either debit or credit")
    @JsonIgnore
    public boolean isValidSide(){
        return normalSide in ["debit","credit"]
    }

    @JsonSetter("normal_side")
    public void setNormalSide(String normalSide){
        this.normalSide = normalSide.toLowerCase()
    }
}
