package org.spsu.accounting.data.dao.impl

import org.spsu.accounting.data.dao.PermissionDAO
import org.spsu.accounting.data.dbi.PermissionDBI
import org.spsu.accounting.data.domain.PermissionSet

/**
 * Created by bpeel on 3/15/15.
 */
class PermissionDAOImpl implements PermissionDAO{

    PermissionDBI dbi

    @Override
    void loadPermissions() {
        PermissionSet.addPermissions(dbi.getAll())
    }
}
