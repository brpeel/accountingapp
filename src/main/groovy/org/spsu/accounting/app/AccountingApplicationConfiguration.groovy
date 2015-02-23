package org.spsu.accounting.app

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory
import org.spsu.accounting.utils.mail.MailConfig

import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * Created by bpeel on 1/28/15.
 */
class AccountingApplicationConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    DataSourceFactory database = new DataSourceFactory();

    @JsonProperty
    @NotNull
    MailConfig mail

}
