package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by bpeel on 3/4/15.
 */
class TestDO extends BaseDO {

    String field1

    @JsonProperty("field_2")
    String field2

    String field3
}
