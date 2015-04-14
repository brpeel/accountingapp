package org.spsu.accounting.data.domain

import org.junit.Ignore
import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dao.impl.PermissionDAOImpl
import org.spsu.accounting.data.dbi.PermissionDBI
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by bpeel on 3/14/15.
 */

class PermissionTest extends Specification {
    void setup() {
        PermissionSet.permissions.clear()
        PermissionSet.addPermission(new Permission(group: "MainMenu", permission: "CreateAccount", role: 100))
        PermissionSet.addPermission(new Permission(group: "Transaction", permission: "ApproveTransaction", role: 50))
        PermissionSet.addPermission(new Permission(group: "Transaction", permission: "CreateTransaction", role: 10))
        PermissionSet.addPermission(new Permission(group: "MainMenu", permission: "transactions", role: 10))

    }

    @Unroll("#featureName : #role")
    def "More users at higher permission levels can do everything that users at lower levels can"() {
        when:
        def result = PermissionSet.getPermissions(role)

        then:
        result.size() == numPermissions

        where:
        role | numPermissions
        100  | 4
        50   | 3
        10   | 2

    }

    @Unroll("#featureName : #role")
    def "Permissions can be retrieved by group"() {
        when:
        Map result = PermissionSet.getPermissionsByGroup(role)

        then:
        result.MainMenu?.size() == mainMenuPermissions
        result.Transaction?.size() == transactionPermissions

        where:
        role | mainMenuPermissions | transactionPermissions
        100  | 2                   | 2
        50   | 1                   | 2
        10   | 1                   | 1

    }

    def "Test Load Permissions"() {
        setup:
        DBConnection db = DBConnection.openConnection("PermissionTest")
        PermissionDBI dbi = db.onDemand(PermissionDBI)
        PermissionDAOImpl dao = new PermissionDAOImpl(dbi: dbi)

        when:
        dao.loadPermissions()
        int adminPerms = PermissionSet.getPermissions(100)?.size()
        int managerPerms = PermissionSet.getPermissions(50)?.size()
        int userPerms = PermissionSet.getPermissions(10)?.size()

        then:
        userPerms > 0
        managerPerms > userPerms
        adminPerms >  managerPerms


    }
}
