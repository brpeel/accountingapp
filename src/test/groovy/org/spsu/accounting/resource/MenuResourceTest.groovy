package org.spsu.accounting.resource

import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dao.impl.UserDAOImpl
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.UserDO
import spock.lang.Specification

/**
 * Created by bpeel on 3/23/15.
 */
class MenuResourceTest extends Specification {

    MenuResource resource
    UserDAOImpl dao
    UserDBI dbi


    void setup(){
        dbi = DBConnection.onDemand(UserDBI)
        dao = new UserDAOImpl<UserDO> (dbi: dbi)

        resource = new MenuResource(dao: dao)
    }

    def "GetActions"() {
        given:
        int userid = 1

        when:
        Map data = resource.determinePermissions(userid)
        println data.toMapString()

        then:
        data?.menuItems != null
        data?.permissions != null

    }
}
