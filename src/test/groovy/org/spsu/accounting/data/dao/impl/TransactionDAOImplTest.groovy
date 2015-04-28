package org.spsu.accounting.data.dao.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.joda.time.DateTime
import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dbi.DocumentDBI
import org.spsu.accounting.data.dbi.PermissionDBI
import org.spsu.accounting.data.dbi.TransactionDBI
import org.spsu.accounting.data.dbi.TransactionEntryDBI
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.TransactionEntryDO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.resource.TransactionResource
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.ValidatorFactory
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 3/22/15.
 */
@Unroll
class TransactionDAOImplTest extends Specification {

    //static TransactionDBI stubbedDBI

    static DBConnection db
    static TransactionDBI h2dbi

    TransactionDAOImpl dao
    TransactionDBI dbi
    TransactionEntryDBI entryDBI
    DocumentDBI documentDBI

    TransactionDO transaction
    TransactionResource resource

    void setupSpec() {
        db = DBConnection.openConnection("TransDAO")

        PermissionDAOImpl pDao = new PermissionDAOImpl(dbi: db.onDemand(PermissionDBI))
        pDao.loadPermissions()

        h2dbi = db.onDemand(TransactionDBI)
    }

    TransactionDO newTrans(Integer id) {
        TransactionDO transaction
        if (id)
            transaction = new TransactionDO(id: id, reportedBy: 1, description: "Test transaction")
        else
            transaction = new TransactionDO(reportedBy: 1, description: "Test transaction")

        transaction.entries = []
        transaction.entries.add(new TransactionEntryDO(amount: 100, debit: false, accountId: 1))
        transaction.entries.add(new TransactionEntryDO(amount: 100, debit: true, accountId: 2))

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

    @Unroll("#featureName : #name")
    def "Debits must equal credits for each transaction"() {

        given:
        dbi.get(_) >> null

        when:
        transaction.entries = [creditEntry, debitEntry]
        Set msg = dao.validateObject(transaction)

        then:
        !expectedMsg || msg?.contains(expectedMsg)

        where:
        name                          | creditEntry                                                     | debitEntry                                                     | expectedMsg
        "No transactions"             | null                                                            | null                                                           | "Transaction must have debt and credit account entries"
        "No debits"                   | null                                                            | new TransactionEntryDO(accountId: 2, amount: 200, debit: true) | "Transaction must have debt and credit account entries"
        "No credits"                  | new TransactionEntryDO(accountId: 1, amount: 100, debit: false) | null                                                           | "Transaction must have debt and credit account entries"
        "Debits not equal to Credits" | new TransactionEntryDO(accountId: 1, amount: 100, debit: false) | new TransactionEntryDO(accountId: 2, amount: 200, debit: true) | "Total credits must equal total debits"
        "Debits equal credits"        | new TransactionEntryDO(accountId: 3, amount: 100, debit: false) | new TransactionEntryDO(accountId: 4, amount: 100, debit: true) | null

    }

    def "System should allow recording of more than one debit or credit for each transaction"() {
        given:
        transaction.entries.add(new TransactionEntryDO(amount: 200, debit: false, accountId: 3))
        transaction.entries.add(new TransactionEntryDO(amount: 200, debit: true, accountId: 4))

        when:
        dao.create(transaction)

        then:
        1 * dbi.insert(_)
        4 * entryDBI.insert(_)
    }


    def "Each transaction will automatically be assigned the current date and time when the transaction was recorded"() {

        given:
        dao.dbi = h2dbi
        transaction.reportedBy = 1
        int transId = dao.create(transaction)

        when:
        TransactionDO trans = dao.get(transId)

        then:
        trans.reported != null
    }

    def "Transactions can be searched by transaction id, date range, or key word"() {
        given:
        dao.dbi = h2dbi

        DateTime past = new DateTime("2015-02-01")
        DateTime future = new DateTime("2015-04-01")

        transaction.reportedBy = 1

        dao.create(transaction)
        int id = db.maxFieldValue("accounting_trans", "id")
        int oldid = dao.create(newTrans())
        int newid = dao.create(newTrans())
        int diffname = dao.create(newTrans())

        db.execute("update accounting_trans set reported = '2015-03-15' where id = $id")
        db.execute("update accounting_trans set id = 1010, reported = '2015-01-01' where id = $oldid")
        db.execute("update accounting_trans set id = 1011, reported = '2016-01-01' where id = $newid")
        db.execute("update accounting_trans set id = 1012, description = 'Some other desc' where id = $diffname")

        when:
        def all = dao.all()
        def results = dao.search(id, "Test", past, future)
        def result = results.get(0)

        then:
        results?.size() == 1
        result?.description?.toLowerCase() =~ "test"
    }

    def "Managers can approve transactions"() {
        given:
        UserDO user = new UserDO(id: 2, role: 50)

        dao.dbi = h2dbi
        transaction.reportedBy = 1
        int transId = dao.create(transaction)

        when:
        dao.approve(transId, user)
        transaction = dao.get(transId)

        then:
        transaction.status == "Approved"
        transaction.approvedBy == user.id
        transaction.approved != null
    }

    def "Managers can reject transactions"() {
        given:
        UserDO user = new UserDO(id: 2, role: 50)

        dao.dbi = h2dbi
        transaction.reportedBy = 1
        int transId = dao.create(transaction)

        when:
        dao.reject(transId, user)
        transaction = dao.get(transId)

        then:
        transaction.status == "Rejected"
        transaction.approved == null
        transaction.approvedBy == null
    }

    def "Ordinary Users cannot approve transactions"() {
        given:
        UserDO user = new UserDO(id: 3, role: 10)

        when:
        dao.approve(1, user)

        then:
        0 * dbi.update(_)
        WebApplicationException exception = thrown(WebApplicationException)
        exception.response.status == Response.Status.FORBIDDEN.statusCode
    }

    def "Ordinary Users cannot reject transactions"() {
        given:
        UserDO user = new UserDO(id: 3, role: 10)

        when:
        dao.reject(1, user)

        then:
        0 * dbi.update(_)

        WebApplicationException exception = thrown(WebApplicationException)
        exception.response.status == Response.Status.FORBIDDEN.statusCode
    }

    def "Transaction must never allow debiting and crediting of the same account in a given transaction"() {
        given:
        transaction.entries.add(new TransactionEntryDO(amount: 200, debit: false, accountId: 1))
        transaction.entries.add(new TransactionEntryDO(amount: 200, debit: true, accountId: 1))


        when:
        Set<String> msgs = dao.validateObject(transaction)
        Set<String> msgSet = msgs?.toSet()

        then:
        msgSet.contains("Transaction must never allow debiting and crediting of the same account")
    }

}
