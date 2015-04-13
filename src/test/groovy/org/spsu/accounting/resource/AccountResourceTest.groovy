package org.spsu.accounting.resource

import org.spsu.accounting.data.dao.ActiveDAO
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 3/5/15.
 */
class AccountResourceTest extends Specification {

    AccountResource resource
    DAO dao
    UserDAO userDAO

    void setup() {
        dao = Mock(ActiveDAO)
        dao.create(_) >> 1000

        userDAO = Mock(UserDAO)
        userDAO.get(_) >> new UserDO(id:1)

        resource = new AccountResource(dao: dao, userDAO: userDAO)
    }

    def "create Account"() {
        given:
        def request = Mock(HttpServletRequest)
        request.getAttribute("userid") >> 1

        when:
        def req = ["name":name, "initialBalance":initialBalance,"normalSide":normalSide, "subcategory":subcategory]
        Response response = resource.create(request,req)

        then:
        response.status == Response.Status.CREATED.statusCode

        where:
        name              | initialBalance | normalSide | subcategory
        "Service Revenue" | 0              | "Debit"    | "revenue"
        "Service Expense" | 1000           | "Debit"    | "revenue"

    }

    def "create Account Empty Body"() {
        given:
        def request = Mock(HttpServletRequest)

        when:
        resource.create(request,null)

        then:
        def exception = thrown(WebApplicationException)
        exception != null && exception.response.status == Response.Status.BAD_REQUEST.statusCode
    }


    def "create Account No user"() {
        given:
        def request = Mock(HttpServletRequest)
        request.getAttribute("userid") >> null

        when:
        def req = ["name":"Some Name", "initialBalance":0,"normalSide":"credit", "subcategory":"cat"]
        resource.create(request,req)

        then:
        def exception = thrown(WebApplicationException)
        exception != null && exception.response.status == Response.Status.UNAUTHORIZED.statusCode
    }
}
