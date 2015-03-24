package org.spsu.accounting.data.dao.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.joda.time.DateTime
import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dbi.DocumentDBI
import org.spsu.accounting.data.dbi.TransactionDBI
import org.spsu.accounting.data.dbi.TransactionEntryDBI
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.TransactionEntryDO
import org.spsu.accounting.resource.TransactionResource
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

    TransactionDO newTrans(Integer id){
        TransactionDO transaction
        if (id)
            transaction = new TransactionDO(id: id, reportedBy: 1, description: "Test transaction")
        else
            transaction = new TransactionDO(reportedBy: 1, description: "Test transaction")

        transaction.entries = []
        transaction.entries.add(new TransactionEntryDO(amount: 100, debit: false, accountId: 1))
        transaction.entries.add(new TransactionEntryDO(amount: 100, debit: true, accountId: 1))

        return transaction
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

        transaction = newTrans(1)
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
        1 * documentDBI.delete(_,_)
    }

    def "Each transaction will automatically be assigned the current date and time when the transaction was recorded"() {

        given:
        dao.dbi = DBConnection.onDemand(TransactionDBI)
        DBConnection.clearTable("accounting_trans")

        transaction.reportedBy = 1
        int transId = dao.create(transaction)

        when:
        TransactionDO trans = dao.get(transId)

        then:
        trans.reported != null
    }

    def "Transactions can be searched by transaction id, date range, or key word"() {
        given:
        dao.dbi = DBConnection.onDemand(TransactionDBI)
        DBConnection.clearTable("accounting_trans")

        DateTime past = new DateTime("2015-02-01")
        DateTime future = new DateTime("2015-04-01")

        transaction.reportedBy = 1

        dao.create(transaction)
        int id = DBConnection.maxFieldValue("accounting_trans", "id")
        int oldid = dao.create(newTrans())
        int newid = dao.create(newTrans())
        int diffname = dao.create(newTrans())

        DBConnection.execute("update accounting_trans set id = 10, reported = '2015-01-01' where id = $oldid")
        DBConnection.execute("update accounting_trans set id = 11, reported = '2016-01-01' where id = $newid")
        DBConnection.execute("update accounting_trans set id = 12, description = 'Some other desc' where id = $diffname")

        when:
        def all = dao.all()
        def results = dao.search(id, "Test", past, future)
        def result = results.get(0)

        then:
        results?.size() == 1
        result?.id?.toString() =~ "1"
        result?.description?.toLowerCase() =~ "test"
    }

}
