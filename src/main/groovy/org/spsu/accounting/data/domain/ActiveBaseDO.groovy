package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by brettpeel on 2/8/15.
 */
class ActiveBaseDO extends BaseDO {

    @JsonProperty("active")
    boolean active = true

}
