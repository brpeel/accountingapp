package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.databind.ObjectMapper
import org.joda.time.DateTime

public class TransactionDO extends BaseDO{


	@JsonProperty("reported_by")
	Integer reportedBy

	@JsonProperty("approved_by")
	Integer approvedBy

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
/*
*
*
* transactionId

	@JsonProperty("accountid")
	int accountId

	@JsonProperty("amount")
	Float amount

    @JsonProperty("debit")
    boolean debit*/
    @JsonSetter("entries")
    public void setEntries(Collection col){

        ObjectMapper mapper = new ObjectMapper()

        entries = []
        if (!col || col.size() == 0)
            return;
        col.each {def e ->
            if (e instanceof Map)
                entries.add(new TransactionEntryDO(id:e.id, transactionId: e.transid, accountId: e.accountid, amount: e.amount, debit: e.debit))
            else
                entries.add(e)
        }
    }

    Set<Integer> debitAccounts(){
        Set<Integer> accounts = new HashSet<Integer>()
        entries?.each {def entry ->
            if (entry && entry.debit)
                accounts.add(entry.accountId)
        }
        return accounts
    }


    Set<Integer> creditAccounts(){
        Set<Integer> accounts = new HashSet<Integer>()
        entries?.each {def entry ->
            if (entry && !entry.debit)
                accounts.add(entry.accountId)
        }
        return accounts
    }


    Float sumDebits(){
        float amount = 0
        if (!entries)
            return 0;
        entries?.each {def entry ->
            if (entry && entry.debit)
                amount += entry.amount
        }

        return amount
    }

    Float sumCredits(){
        float amount = 0
        if (!entries)
            return 0;
        entries?.each {def entry ->
            if (entry && !entry.debit)
                amount += entry.amount
        }

        return amount
    }

    public boolean isSubmitted(){
        return status?.toLowerCase() in ["submitted", "approved"]
    }

    @JsonGetter
    public Float debits(){
        Float debits = sumDebits()
        return debits
    }

    @JsonGetter
    public Float credits(){
        Float debits = sumCredits()
        return debits
    }

    @JsonGetter
    public String accounts(){
        HashSet<Integer> accounts = new HashSet<>()

        entries?.each {def entry ->
            if (entry)
                accounts.add(entry.accountId)
        }

        String accountStr = accounts.join(",")
        return accountStr
    }
}
