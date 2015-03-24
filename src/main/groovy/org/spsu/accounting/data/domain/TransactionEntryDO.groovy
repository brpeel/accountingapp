package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty

public class TransactionEntryDO extends BaseDO{

	@JsonProperty("transid")
	int transactionId

	@JsonProperty("accountid")
	int accountId

	@JsonProperty("amount")
	Float amount

    @JsonProperty("debit")
    boolean debit
}
