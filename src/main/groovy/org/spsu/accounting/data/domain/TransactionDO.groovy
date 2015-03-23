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

    @JsonProperty("description")
    String description

    List<TransactionEntryDO> entries = []

    List<DocumentDO> documents = []

    Float sumDebits(){
        float amount = 0
        if (!entries)
            return 0;
        entries?.each {TransactionEntryDO  entry ->
            if (entry && entry.isDebit())
                amount += entry.amount
        }

        return amount
    }

    Float sumCredits(){
        float amount = 0
        if (!entries)
            return 0;
        entries?.each {TransactionEntryDO  entry ->
            if (entry && !entry.isDebit())
                amount += entry.amount
        }

        return amount
    }

    public boolean isSubmitted(){
        return status?.toLowerCase() in ["submitted", "approved"]
    }

}
