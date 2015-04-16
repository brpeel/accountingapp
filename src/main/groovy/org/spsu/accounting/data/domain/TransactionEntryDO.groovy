package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.spsu.accounting.data.serial.MoneySerializer

import javax.validation.constraints.NotNull

public class TransactionEntryDO extends BaseDO{

	@JsonProperty("transid")
	int transactionId

	@JsonProperty("accountid")
    @NotNull
	int accountId

	@JsonProperty("amount")
    @JsonSerialize(using = MoneySerializer.class)
    @NotNull
	BigDecimal amount

    @JsonProperty("debit")
    @NotNull
    boolean debit
}
