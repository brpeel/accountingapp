package org.spsu.accounting.utils

import spock.lang.Specification

/**
 * Created by bpeel on 2/24/15.
 */
class AuthUtilsTest extends Specification {
    def "GetHash"() {

        given:
        String str = "password"

        when:
        String hash = AuthUtils.getHash(str)
        println hash
        println hash.length()

        then:
        hash == "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"

    }
}
