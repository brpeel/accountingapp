package org.spsu.accounting.resource.base

import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.domain.TestDO
import spock.lang.Specification

/**
 * Created by bpeel on 3/4/15.
 */
class BaseResourceTest extends Specification {

    void setup() {
    }

    def "SetObjectValues"() {
        given:
        TestResource resource = new TestResource()
        TestDO testDO = new TestDO(field1:"field1 original", field2: "field2 original", field3: "field3 original")
        Map values = ["field1":"new field 1", "badfield":"nothing"]

        when:
        testDO = resource.setObjectValues(testDO, values)

        then:
        testDO.field1 == "new field 1"
    }
}
