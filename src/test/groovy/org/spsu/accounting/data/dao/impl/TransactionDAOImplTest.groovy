package org.spsu.accounting.data.dao.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dbi.DocumentDBI
import org.spsu.accounting.data.dbi.TransactionDBI
import org.spsu.accounting.data.dbi.TransactionEntryDBI
import org.spsu.accounting.data.domain.DocumentDO
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.TransactionEntryDO
import org.spsu.accounting.resource.TransactionResource
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.ValidatorFactory

/**
 * Created by bpeel on 3/22/15.
 */
@Unroll
class TransactionDAOImplTest extends Specification {

    //static TransactionDBI stubbedDBI

    TransactionDAOImpl dao
    TransactionDBI dbi
    TransactionEntryDBI entryDBI
    DocumentDBI documentDBI

    TransactionDO transaction
    TransactionResource resource

    void setupSpec(){
        //stubbedDBI = DBConnection.onDemand(TransactionDBI)
    }

    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()

        dbi = Mock(TransactionDBI)//DBConnection.onDemand(TransactionDBI)
        dbi.insert(_) >> 1
        entryDBI = Mock(TransactionEntryDBI)//DBConnection.onDemand(TransactionEntryDBI)
        documentDBI = Mock(DocumentDBI)

        dao = new TransactionDAOImpl(dbi: dbi, entryDBI: entryDBI, documentDBI: documentDBI, objectMapper: new ObjectMapper(), validator: factory.getValidator())

        //Transaction Setup
        dbi.get(_) >> null

        transaction = new TransactionDO(id: 1, reportedBy: 1, description: "Test transaction")
        transaction.entries = []
        transaction.entries.add(new TransactionEntryDO(amount: 100, debit: false, accountId: 1))
        transaction.entries.add(new TransactionEntryDO(amount: 100, debit: true, accountId: 1))
    }

    def "Debits must equal credits for each transaction"() {

        given:
        dbi.get(_) >> null

        when:
        transaction.entries = [creditEntry, debitEntry]
        String msg = dao.validateObject(transaction)?.get(0)

        then:
        msg == expectedMsg

        where:
        creditEntry                                       | debitEntry                                       | expectedMsg
        null                                              | null                                             | "Transaction must have credit and debt account entries"
        new TransactionEntryDO(amount: 100, debit: false) | new TransactionEntryDO(amount: 200, debit: true) | "Total credits must equal total debits"
        new TransactionEntryDO(amount: 100, debit: false) | new TransactionEntryDO(amount: 100, debit: true) | null

    }

    def "System should allow recording of more than one debit or credit for each transaction"() {
        given:
        transaction.entries.add(new TransactionEntryDO(amount: 200, debit: false, accountId: 2))
        transaction.entries.add(new TransactionEntryDO(amount: 200, debit: true, accountId: 2))

        when:
        dao.create(transaction)

        then:
        1 * dbi.insert(_)
        4 * entryDBI.insert(_,_)
    }

    def "The user must have the ability to upload one or more source documents for each transaction"() {
        given:
        documentDBI.get(_,_) >> null

        when:
        dao.addDocument(1, "something")
        dao.addDocument(1, "something new")

        then:
        2 * documentDBI.insert(_,_)
    }

    def "The user must have the ability to remove documents"() {
        given:
        documentDBI.get(_,_) >> null

        when:
        dao.removeDocument(1, "something")

        then:
        2 * documentDBI.delete(_,_)
    }

    def "Each transaction will automatically be assigned the current date and time when the transaction was recorded"() {

        given:
        dao.dbi = DBConnection.onDemand(TransactionDBI)
        transaction.reportedBy = 1
        int transId = dao.create(transaction)

        when:
        TransactionDO trans = dao.get(transId)

        then:
        trans.reported != null
    }

    def "Transactions can be searched by transaction id, date range, or key word"() {
        given:
        dao.dbi = DBConnection.onDemand(TransactionDBI, "jdbc:postgresql://localhost:5432/Accounting", "accounting_user", "accounting_user")
        transaction.reportedBy = 1
        transaction.id = null
        dao.create(transaction)

        when:
        def result = dao.search(1)
        int transId = DBConnection.maxFieldValue("accounting_trans", "id")
        println result

        then:
        false
    }

}
