package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty
import junit.framework.Test

/**
 * Created by brettpeel on 2/8/15.
 */
class BaseDOTest extends spock.lang.Specification {

    class TestDO extends BaseDO {

        String field1

        @JsonProperty("field_2")
        String field2

        String field3
    }

    def TestDO testDO
    void setup() {
        testDO = new TestDO(field1:"field1 original", field2: "field2 original", field3: "field3 original")
    }

    def "TestNewMerge"() {
        given:
        Map changes = ["field1":"field1 changed", "field_2":"field2 changed"]

        when:
        testDO.merge(changes)

        then:
        testDO.field1 == "field1 changed" && testDO.field2 == "field2 changed" && testDO.field3 == "field3 original"
    }

}
