package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime

public class TransactionDO extends BaseDO{

	@JsonProperty("reported_by")
	int reportedBy

	@JsonProperty("approved_by")
	int approvedBy

	@JsonProperty("reported")
	DateTime reported

	@JsonProperty("approved")
	DateTime approved

	@JsonProperty("status")
	String status

}
