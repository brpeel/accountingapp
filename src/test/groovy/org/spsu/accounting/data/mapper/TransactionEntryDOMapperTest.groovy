package org.spsu.accounting.data.mapper

import org.spsu.accounting.data.domain.TransactionEntryDO
import spock.lang.Specification

/**
 * Created by bpeel on 4/11/15.
 */
class TransactionEntryDOMapperTest extends Specification {
    TransactionEntryDOMapper mapper

    void setup() {
        mapper = new TransactionEntryDOMapper()
    }

    def "Convert Amount"() {
        given:

        Map rawData = ["amount":amount]

        when:
        TransactionEntryDO result = mapper.map(rawData)

        then:
        result != null
        result.amount instanceof BigDecimal
        result.amount == expected

        where:
        amount | expected
        0      | BigDecimal.ZERO
        100    | new BigDecimal(100)
        200.00 | new BigDecimal(200.00)
    }


    def "Convert Amount is null safe"() {
        given:

        Map rawData = ["amount":null]

        when:
        TransactionEntryDO result = mapper.map(rawData)

        then:
        result != null
        result.amount == null
    }
}
