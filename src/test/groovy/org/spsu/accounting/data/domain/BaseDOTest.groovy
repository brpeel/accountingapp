package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty
import junit.framework.Test

/**
 * Created by brettpeel on 2/8/15.
 */
class BaseDOTest extends spock.lang.Specification {

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

    def "Test getFields"(){

        when:
        List<String> fields = testDO.getFields().sort()

        then:
        fields == ["field1", "field2", "field3", "id"].sort()
    }

}
