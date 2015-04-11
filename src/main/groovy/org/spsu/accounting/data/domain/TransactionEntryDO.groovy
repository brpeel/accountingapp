package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.spsu.accounting.data.serial.MoneySerializer

public class TransactionEntryDO extends BaseDO{

	@JsonProperty("transid")
	int transactionId

	@JsonProperty("accountid")
	int accountId

	@JsonProperty("amount")
    @JsonSerialize(using = MoneySerializer.class)
	BigDecimal amount

    @JsonProperty("debit")
    boolean debit
}
