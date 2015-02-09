package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime

public class UserDO extends ActiveBaseDO{

	@JsonProperty("username")
	String username

    @JsonIgnore
	String password

	@JsonProperty("first_name")
	String firstName

	@JsonProperty("last_name")
	String lastName

	@JsonProperty("locked")
	boolean locked

	@JsonProperty("password_reset")
	DateTime passwordReset

	@JsonProperty("email")
	String email

	@JsonProperty("login_attempts")
	int loginAttempts

}
