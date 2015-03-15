package org.spsu.accounting.data.mapper

import org.spsu.accounting.data.domain.Permission
import org.spsu.accounting.data.domain.UserRole

/**
 * Created by bpeel on 3/14/15.
 */
class PermissionMapper extends BaseMapper<Permission>{
    @Override
    protected Map convertInputs(Map data) {
        if (data.usertypeid)
            data.usertypeid = UserRole.determineRole(data.usertypeid)
        return data
    }
}
