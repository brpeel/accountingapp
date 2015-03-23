package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty

public class DocumentDO extends BaseDO{

	@JsonProperty("trans_id")
	int transactionId

	@JsonProperty("document_uri")
	String documentUri

}
