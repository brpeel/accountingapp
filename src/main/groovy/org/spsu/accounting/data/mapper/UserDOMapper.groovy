package org.spsu.accounting.data.mapper

import org.postgresql.jdbc4.Jdbc4Array
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.data.domain.UserRole

public class UserDOMapper extends BaseMapper<UserDO>{

    @Override
    protected Map convertInputs(Map data) {

        Jdbc4Array usersRoles = data.roles
        if (usersRoles) {
            final HashSet<UserRole> roles = new HashSet<>()

            usersRoles.array.each { String role ->
                roles.add(UserRole.determineRole(role))
            }
            data.roles = roles
        }
        return data
    }
}
