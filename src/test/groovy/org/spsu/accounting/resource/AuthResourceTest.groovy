package org.spsu.accounting.resource

import org.apache.http.HttpRequest
import org.joda.time.DateTime
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.domain.UserDO
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 3/9/15.
 */
class AuthResourceTest extends Specification {

    AuthResource resource
    UserDAO dao
    Map request

    void setup() {
        dao = Mock(UserDAO)
        resource = new AuthResource(dao)

        request = [username:"bpeel",password:"some password"]
    }

    def "Authenticate - force reset"() {
        given:
        Calendar cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -2)
        long past = cal.getTimeInMillis()

        dao.checkLogin(_,_) >> new UserDO(resetOnLogon:false, passwordSet:new DateTime(past), loginAttempts: 0)
        dao.createSession(_) >> "12345"

        when:
        Response response = resource.authenticate(request)

        then:
        response.status == Response.Status.OK.statusCode
        response.entity?.reset_on_logon == true
    }

    def "Authenticate - max login attempts exceeded"() {
        given:
        Calendar cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -2)
        long past = cal.getTimeInMillis()

        dao.checkLogin(_,_) >> new UserDO(resetOnLogon:false, passwordSet:new DateTime(past), loginAttempts: 3)

        when:
        Response response = resource.authenticate(request)

        then:
        response.status == Response.Status.UNAUTHORIZED.statusCode
        response.entity?.locked == true
    }


    def "Authenticate - invalid user name or password"() {
        given:
        dao.checkLogin(_,_) >> null

        when:
        Response response = resource.authenticate(request)

        then:
        response.status == Response.Status.UNAUTHORIZED.statusCode

    }
}
