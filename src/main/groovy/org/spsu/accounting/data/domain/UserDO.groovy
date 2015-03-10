package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime

public class UserDO extends ActiveBaseDO{

	@JsonProperty("username")
	String username

	@JsonProperty("first_name")
	String firstName

	@JsonProperty("last_name")
	String lastName

	@JsonProperty("email")
	String email

	@JsonProperty("login_attempts")
	int loginAttempts

    @JsonIgnore
    boolean resetOnLogon

}
