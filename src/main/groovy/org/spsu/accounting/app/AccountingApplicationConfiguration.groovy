package org.spsu.accounting.app

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory

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

}
