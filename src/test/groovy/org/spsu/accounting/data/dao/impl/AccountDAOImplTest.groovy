package org.spsu.accounting.data.dao.impl

import org.junit.Ignore
import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dbi.AccountDBI
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.AccountDO
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validator

/**
 * Created by bpeel on 3/28/15.
 */
@Ignore
class AccountDAOImplTest extends Specification {

    AccountDAOImpl dao
    AccountDBI dbi


    void setup() {
        dbi = DBConnection.onDemand(AccountDBI)

        Validator validator = Mock(Validator)
        validator.validate(_) >> new HashSet<ConstraintViolation<AccountDO>>()

        dao = new AccountDAOImpl(dbi: dbi, validator: validator)
    }

    def "All assets account ids begin with 1"() {
        given:
        AccountDO account = new AccountDO(name: "New Account", initialBalance: 10.0, normalSide: "Credit", addedBy:1, category:"Asset", subcategory:"Long term asset")

        when:
        int id = dao.create(account)

        then:
        id != 0 && "$id".startsWith("1")
    }

    def "All liabilities account ids begin with 2"() {
        given:
        AccountDO account = new AccountDO(name: "New Account", initialBalance: 10.0, normalSide: "Credit", addedBy:1, category:"Liability", subcategory:"Long term Liability")

        when:
        int id = dao.create(account)

        then:
        id != 0 && "$id".startsWith("2")
    }

    def "All owner’s equity account ids begin with 3"() {
        given:
        AccountDO account = new AccountDO(name: "New Account", initialBalance: 10.0, normalSide: "Credit", addedBy:1, category:"Owner Equity", subcategory:"Long term equity")

        when:
        int id = dao.create(account)

        then:
        id != 0 && "$id".startsWith("3")
    }

    def "All expense account ids begin with 4"() {
        given:
        AccountDO account = new AccountDO(name: "New Account", initialBalance: 10.0, normalSide: "Credit", addedBy:1, category:"Expense", subcategory:"Long term expense")

        when:
        int id = dao.create(account)

        then:
        id != 0 && "$id".startsWith("4")
    }

    def "All revenue account ids begin with 5"() {
        given:
        AccountDO account = new AccountDO(name: "New Account", initialBalance: 10.0, normalSide: "Credit", addedBy:1, category:"Revenue", subcategory:"Long term revenue")

        when:
        int id = dao.create(account)

        then:
        id != 0 && "$id".startsWith("5")
    }
}
