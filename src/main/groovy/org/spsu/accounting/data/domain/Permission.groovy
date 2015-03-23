package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF

/**
 * Created by bpeel on 3/14/15.
 */
final class Permission {

    @JsonProperty("permission_group")
    String group

    @JsonProperty("permission")
    String permission

    @JsonProperty("group_order")
    int order

    @JsonProperty("user_type_id")
    UserRole minRole

    @JsonProperty("label")
    String label

    @JsonProperty("style")
    String style

    @JsonProperty("active")
    boolean active = true

    @Override
    boolean equals(Object obj) {
        if (!obj)
            return false
        return obj && obj instanceof Permission && obj.hashCode() == hashCode()
    }

    @Override
    int hashCode() {
        return (!permission ? 0 : permission.hashCode())
    }

    @JsonSetter("minRole")
    public void setMinRole(String role){
        minRole = UserRole.determineRole(role)
    }
}


public enum PermissionSet {

    INSTANCE

    private final static HashMap<UserRole, HashSet<Permission>> permissions = [:]

    public static void addPermissions(List<Permission> perms){
        if (perms == null)
            return;
        perms.each(){ Permission p -> addPermission(p)}
    }

    public static void addPermission(Permission permission){
        Set rolePerms = permissions.get(permission.minRole)
        if (rolePerms == null){
            rolePerms = new HashSet<Permission>()
            permissions.put(permission.minRole, rolePerms)
        }
        rolePerms.add(permission)
    }

    public static boolean hasPermission(UserRole role, Permission permission){
        Set<Permission> rolePerms = getPermissions(role)
        return rolePerms && rolePerms.contains(permission)
    }

    public static boolean hasPermission(UserRole role, String permission){
        return hasPermission(role, new Permission(permission))
    }

    public static Set<Permission> getPermissions(UserRole role){
        final Set<Permission> rolePerms = []

        permissions.collect {UserRole uRole, Set perms ->
            if(uRole <= role)
               rolePerms.addAll(perms)
        }

        return rolePerms
    }

    public static Map<String, Set<Permission>> getPermissionsByGroup(UserRole role){
        Set rolePerms = getPermissions(role)
        final Map<String, Set<Permission>> groupPermissions = [:]

        rolePerms.each { Permission permission ->
            Set perms = groupPermissions.get(permission.group)
            if (perms == null){
                perms = new HashSet<Permission>()
                groupPermissions.put(permission.group, perms)
            }
            perms.add(permission)
        }

        return groupPermissions
    }
 }