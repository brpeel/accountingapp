package org.spsu.accounting.data.dbi

import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.domain.AccountDO
import spock.lang.Shared
import spock.lang.Specification

import java.sql.Timestamp

/**
 * Created by bpeel on 3/3/15.
 */
class AccountDBITest extends Specification {

    final static Timestamp now = new Timestamp(System.currentTimeMillis())
    @Shared AccountDBI dbi

    @Shared AccountDO account1 = new AccountDO(name: "Service Revenue", initialBalance: 0, normalSide: 'debit', active: true, addedBy: 1, subcategory: "Revenue")
    @Shared AccountDO account2 = new AccountDO(name: "Investment Revenue", initialBalance: 1000, normalSide: 'debit', active: true, addedBy: 1, subcategory: "Revenue")
    @Shared AccountDO account3 = new AccountDO(name: "Investment Inactive", initialBalance: 1000, normalSide: 'debit', active: true, addedBy: 1, subcategory: "Revenue")

    void setup() {
        dbi = DBConnection.onDemand(AccountDBI)

        account1.id = dbi.insert(account1)
        account2.id = dbi.insert(account2)
        account3.active = false
        account3.id = dbi.insert(account3)
    }

    def "Get"() {
        given:
        Timestamp now = new Timestamp(System.currentTimeMillis())
        AccountDO account = new AccountDO(name: "Service Revenue", initialBalance: 0, normalSide: 'debit', active: true, addedBy: 1, subcategory: "Revenue")
        int id = dbi.insert(account)

        when:
        AccountDO result = dbi.get(1)

        then:
        result != null && result.id == 1
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
        account1.name = "New Account"
        dbi.update(account1)
        AccountDO acc = dbi.get(1)

        then:
        acc.name == "New Account"
    }

    def "Delete"() {

        given:
        int originalCount = (dbi.getAll()).size()

        when:
        account1.active = false
        dbi.update(account1)
        int count = (dbi.getAll()).size()

        then:
        count < originalCount
    }
}
