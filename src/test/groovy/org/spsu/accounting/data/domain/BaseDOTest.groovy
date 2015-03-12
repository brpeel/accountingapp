package org.spsu.accounting.data.domain
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
        Map changes = ["id":1,"field1":"field1 changed", "field_2":"field2 changed"]

        when:
        testDO.merge(changes)

        then:
        testDO.id == 1 && testDO.field1 == "field1 changed" && testDO.field2 == "field2 changed" && testDO.field3 == "field3 original"
    }

    def "Test getFields"(){

        when:
        Map<String,String> fields = testDO.listFieldsForMerge()
        println fields
        then:
        fields == ["id":"id", "field3":"field3", "field2":"field2", "field_2":"field2", "field1":"field1"]
    }

}
