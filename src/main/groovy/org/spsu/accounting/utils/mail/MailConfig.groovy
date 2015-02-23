package org.spsu.accounting.utils.mail

import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull

/**
 * Created by brettpeel on 2/22/15.
 */
class MailConfig {

    @JsonProperty
    @NotNull
    String username = "swe4713brpeel@gmail.com"

    @JsonProperty
    @NotNull
    String password = "spsu2015"

    @JsonProperty
    @NotNull
    int port = 587

    @JsonProperty
    @NotNull
    String host = "smtp.gmail.com"

    @JsonProperty
    @NotNull
    boolean notifyStart = false
}
