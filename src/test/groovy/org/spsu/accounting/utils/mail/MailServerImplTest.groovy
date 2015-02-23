package org.spsu.accounting.utils.mail

import spock.lang.Specification

/**
 * Created by brettpeel on 2/22/15.
 */
class MailServerImplTest extends Specification {
    void setup() {

    }

    def "Send"() {
        given:
        MailServer server = new MailServerImpl(new MailConfig())
        when:
        server.send("bpeel56@gmail.com", "Testing mail server", "This is just a test")

        then:
        true

    }
}
