package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response

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

    @JsonProperty("role")
    int role

    public boolean hasPermission(String permission){
        return PermissionSet.hasPermission(role, permission)
    }

    public void requirePermission(String permission){
        if (!hasPermission(permission))
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());

    }
}
