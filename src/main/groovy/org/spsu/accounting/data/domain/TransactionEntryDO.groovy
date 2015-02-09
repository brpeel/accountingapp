package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty

public class TransactionEntryDO extends BaseDO{

	@JsonProperty("transaction_id")
	int transactionId

	@JsonProperty("account_id")
	int accountId

	@JsonProperty("account")
	Float amount

}
