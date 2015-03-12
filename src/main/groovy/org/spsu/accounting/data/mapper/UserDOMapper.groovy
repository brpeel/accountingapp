package org.spsu.accounting.data.mapper

import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.data.domain.UserRole

public class UserDOMapper extends BaseMapper<UserDO>{

    @Override
    protected Map convertInputs(Map data, Map rawData) {
        final HashSet<UserRole> roles = new HashSet<>()

        rawData.roles?.each { String role ->
            roles.add(UserRole.determineRole(role))
        }

        data.roles = roles
        return data
    }
}
