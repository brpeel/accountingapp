package org.spsu.accounting.data.dao.impl

import org.junit.Ignore
import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dbi.PermissionDBI
import org.spsu.accounting.data.domain.PermissionSet
import spock.lang.Specification

/**
 * Created by bpeel on 3/23/15.
 */
@Ignore
class PermissionDAOImplTest extends Specification {

    PermissionDAOImpl dao
    PermissionDBI dbi
    void setup(){
        dbi = DBConnection.onDemand(PermissionDBI)
        dao = new PermissionDAOImpl(dbi: dbi)
    }

    def "LoadPermissions"() {

        when:
        dao.loadPermissions()

        then:
        PermissionSet.permissions?.size() > 0

    }
}
