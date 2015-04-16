package org.spsu.accounting.resource

import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dao.TransactionDAO
import org.spsu.accounting.data.dao.UserDAO
import spock.lang.Specification

import javax.ws.rs.core.Response

/**
 * Created by bpeel on 3/29/15.
 */
class TransactionResourceTest extends Specification {

    TransactionResource resource
    UserDAO userDAO
    TransactionDAO dao
    static DBConnection db

    void setupSpec() {
        db = DBConnection.openConnection("TransResource")

    }

    void setup() {
        resource = new TransactionResource()
    }

    def "Submit"() {

    }

    def "Approve"() {

    }

    def "Reject"() {

    }


    def "Get Transactions by account"() {
        given:
        resource.accountTransDBI = db.onDemand(TransactionResource.AccountTransDBI)

        when:
        Response response = resource.getTransForAccount(101)

        then:
        response?.entity?.size() > 0
    }
}
