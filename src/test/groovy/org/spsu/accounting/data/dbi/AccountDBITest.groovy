package org.spsu.accounting.data.dbi

import org.junit.Ignore
import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.domain.AccountDO
import spock.lang.Specification

import java.sql.Timestamp

/**
 * Created by bpeel on 3/3/15.
 */
class AccountDBITest extends Specification {

    final static Timestamp now = new Timestamp(System.currentTimeMillis())
    static int minId
    static AccountDBI dbi

    static AccountDO account1 = new AccountDO(name: "Service Revenue", initialBalance: 0, normalSide: 'debit', active: true, addedBy: 1, category: "Revenue")
    static AccountDO account2 = new AccountDO(name: "Investment Revenue", initialBalance: 1000, normalSide: 'debit', active: true, addedBy: 1, category: "Revenue")
    static AccountDO account3 = new AccountDO(name: "Investment Inactive", initialBalance: 1000, normalSide: 'debit', active: true, addedBy: 1, category: "Revenue")

    void setupSpec() {
        DBConnection db = DBConnection.openConnection("AccountDBI")
        dbi = db.onDemand(AccountDBI)
        db.clearTable("accounting_trans_entry")
        db.clearTable("account")

        dbi.insert(account1)
        account1.id = db.maxFieldValue("account","id")

        dbi.insert(account2)
        account2.id = db.maxFieldValue("account","id")

        account3.active = false
        dbi.insert(account3)
        account3.id = db.maxFieldValue("account","id")

        minId = db.minFieldValue("account","id")
    }

    def "Get"() {

        when:
        AccountDO result = dbi.get(minId)

        then:
        result != null && result.id == minId
    }

    def "GetAll"() {

        when:
        List<AccountDO> result = dbi.getAll()
        AccountDO a1 = result[0]
        AccountDO a2 = result[1]

        then:
        a1.name == "Service Revenue"
        a2.name == "Investment Revenue"
    }

    def "Update"() {

        when:
        AccountDO acc = account2
        acc.name = "New Account"
        dbi.update(acc)
        AccountDO result = dbi.get(acc.id)

        then:
        result.name == "New Account"
    }

    def "Delete"() {

        given:
        int originalCount = (dbi.getAll()).size()
        AccountDO acc = dbi.get(minId)

        when:
        acc.active = false
        dbi.update(acc)
        int count = (dbi.getAll()).size()

        then:
        count < originalCount
    }
}
