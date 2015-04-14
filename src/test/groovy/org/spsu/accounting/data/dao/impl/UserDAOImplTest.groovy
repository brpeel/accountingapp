package org.spsu.accounting.data.dao.impl

import org.junit.Ignore
import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.utils.mail.MailServer
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.Timestamp

/**
 * Created by bpeel on 3/5/15.
 */
class UserDAOImplTest extends Specification {

    @Shared
    UserDAOImpl dao
    @Shared
    UserDBI dbi
    @Shared
    UserDO user = new UserDO(id: 1, email: "someemail@server.com")
    @Shared
    MailServer mailServer

    void setup() {
        dbi = Mock(UserDBI)

        dao = new UserDAOImpl(dbi: dbi)

        mailServer = Mock(MailServer)
        mailServer.send(_, _, _) >> { println "Sending email" }
        mailServer.send(_) >> { println "Sending email" }

        dao.mailServer = mailServer

    }

    @Unroll("#msg")
    def "Password not valid"() {
        given:
        Set<String> last5Pwd = new HashSet<>()
        last5Pwd.add("a0783e62371a7ac4cac91803231e635f7584a8a43f32b78c827bf8444137bd41")
        dbi.last5Passwords(_) >> last5Pwd

        when:
        dao.setPassword(user, password)

        then:
        def exception = thrown(Exception)
        exception.message == msg

        where:
        user | password                | msg
        user | null                    | "Password cannot be empty"
        user | ""                      | "Password cannot be empty"
        user | "     "                 | "Password cannot be empty"
        user | "noFirstCapital"        | "Password must start with a capital letter"
        user | "short"                 | "Password must be at least 8 characters long"
        user | "Nonumbers"             | "Password must contain letters and numbers"
        user | "123123123123123"       | "Password must contain letters and numbers"
        user | "SuperValidPassword123" | "Password cannot match one of the previous 5 passwords"

    }

    def "Send has password and send email when user updates their password"() {

        when:
        dao.setPassword(user, "SuperValidPassword123")

        then:
        1 * mailServer.send(_, _, _)
        1 * dbi.updatePassword(_, _)
    }

    def "Send email when admin resets a users password"() {

        when:
        dao.resetPassword(user)

        then:
        1 * mailServer.send(_, _, _)
        1 * dbi.resetPassword(_, _)
    }

    @Unroll("#feature #name")
    def "Password expired"() {

        given:
        dbi.getPasswordSet(_) >> time

        when:
        def expired = dao.isPasswordExpired(user)

        then:
        expired == expected

        where:
        name          | user              | time                                      | expected
        "Not expired" | new UserDO(id: 1) | new Timestamp(System.currentTimeMillis()) | false
        "Expired"     | new UserDO(id: 1) | new Timestamp(0)                          | true
        "Null time"   | new UserDO(id: 1) | null                                      | true

    }

    def "Get"(){

        given:
        UserDBI dbi = DBConnection.openConnection("UserDAO").onDemand(UserDBI)

        when:
        UserDO user = dbi.get(1)
        List<UserDO> users = dbi.getAll()

        then:
        users.size() > 0
        user?.role == 100
    }
}
