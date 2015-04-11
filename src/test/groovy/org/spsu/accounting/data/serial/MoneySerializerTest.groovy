package org.spsu.accounting.data.serial

import spock.lang.Specification

/**
 * Created by bpeel on 4/11/15.
 */
class MoneySerializerTest extends Specification {

    def "BigDecimal is serialized to money"(){

        given:
        MoneySerializer serializer = new MoneySerializer()
        BigDecimal amount = new BigDecimal(100);

        when:
        String result =  serializer.convertToString(amount)

        then:
        result == "100.00"
    }
}
