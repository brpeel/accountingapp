package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import org.spsu.accounting.data.serial.DateTimeSerializer
import org.spsu.accounting.data.serial.MoneySerializer

public class TransactionDO extends BaseDO{


	@JsonProperty("reported_by")
	Integer reportedBy

	@JsonProperty("approved_by")
	Integer approvedBy

	@JsonProperty("reported")
    @JsonSerialize(using = DateTimeSerializer)
    DateTime reported

	@JsonProperty("approved")
    @JsonSerialize(using = DateTimeSerializer)
    DateTime approved

	@JsonProperty("status")
	String status

    @JsonProperty("description")
    String description

    List<TransactionEntryDO> entries = []

    List<DocumentDO> documents = []

    @JsonSetter("entries")
    public void setEntries(Collection col){

        ObjectMapper mapper = new ObjectMapper()

        entries = []
        if (!col || col.size() == 0)
            return;
        col.each {def e ->
            if (e instanceof Map)
                entries.add(mapper.convertValue(e, TransactionEntryDO))
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


    BigDecimal sumDebits(){
        BigDecimal amount = 0
        if (!entries)
            return 0;
        entries?.each {def entry ->
            if (entry && entry.debit && entry.amount)
                amount += entry.amount
        }

        return amount
    }

    BigDecimal sumCredits(){
        BigDecimal amount = 0
        if (!entries)
            return 0;
        entries?.each {def entry ->
            if (entry && !entry.debit && entry.amount)
                amount += entry.amount
        }

        return amount
    }

    public boolean isSubmitted(){
        return status?.toLowerCase() in ["submitted", "approved"]
    }

    @JsonGetter
    @JsonSerialize(using = MoneySerializer.class)
    public BigDecimal debits(){
        BigDecimal debits = sumDebits()
        return debits
    }

    @JsonGetter
    @JsonSerialize(using = MoneySerializer.class)
    public BigDecimal credits(){
        BigDecimal debits = sumCredits()
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
