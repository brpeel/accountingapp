package org.spsu.accounting.data.domain

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

        PermissionSet.addPermission(new Permission(group: "MainMenu", permission: "CreateAccount", minRole: UserRole.ADMIN))
        PermissionSet.addPermission(new Permission(group: "Transaction", permission: "ApproveTransaction", minRole: UserRole.MANAGER))
        PermissionSet.addPermission(new Permission(group: "Transaction", permission: "CreateTransaction", minRole: UserRole.USER))
        PermissionSet.addPermission(new Permission(group: "MainMenu", permission: "transactions", minRole: UserRole.USER))

    }

    @Unroll("#featureName : #role")
    def "More users at higher permission levels can do everything that users at lower levels can"() {
        when:
        def result = PermissionSet.getPermissions(role)

        then:
        result.size() == numPermissions

        where:
        role             | numPermissions
        UserRole.ADMIN   | 4
        UserRole.MANAGER | 3
        UserRole.USER    | 2

    }

    @Unroll("#featureName : #role")
    def "Permissions can be retrieved by group"() {
        when:
        Map result = PermissionSet.getPermissionsByGroup(role)

        then:
        result.MainMenu?.size() == mainMenuPermissions
        result.Transaction?.size() == transactionPermissions

        where:
        role             | mainMenuPermissions | transactionPermissions
        UserRole.ADMIN   | 2                   | 2
        UserRole.MANAGER | 1                   | 2
        UserRole.USER    | 1                   | 1

    }

    def "Test Load Permissions"(){
        setup:
        PermissionDBI dbi = DBConnection.onDemand(PermissionDBI)
        PermissionDAOImpl dao = new PermissionDAOImpl(dbi: dbi)

        when:
        dao.loadPermissions()
        Set adminPerms = PermissionSet.getPermissions(UserRole.ADMIN)
        Set managerPerms = PermissionSet.getPermissions(UserRole.MANAGER)
        Set userPerms = PermissionSet.getPermissions(UserRole.USER)

        then:
        adminPerms != null && adminPerms.size() == 22
        managerPerms != null && managerPerms.size() == 16
        userPerms != null && userPerms.size() == 12
    }
}
