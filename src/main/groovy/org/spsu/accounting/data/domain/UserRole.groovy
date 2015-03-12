package org.spsu.accounting.data.domain

/**
 * Created by bpeel on 3/12/15.
 */
enum UserRole {

    USER,
    MANAGER,
    ADMIN

    public static UserRole determineRole(String type){
        type = type?.toLowerCase()

        UserRole userType = USER
        if (type == "manager")
            userType = MANAGER
        if (type == "admin")
            userType = ADMIN
        return userType
    }
}