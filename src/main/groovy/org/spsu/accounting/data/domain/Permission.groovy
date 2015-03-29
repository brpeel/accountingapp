package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonProperty

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
    int role

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
}


public enum PermissionSet {

    INSTANCE

    private final static HashMap<Integer, HashSet<Permission>> permissions = [:]

    public static void addPermissions(List<Permission> perms){
        if (perms == null)
            return;
        perms.each(){ Permission p -> addPermission(p)}
    }

    public static void addPermission(Permission permission){
        Set rolePerms = permissions.get(permission.role)
        if (rolePerms == null){
            rolePerms = new HashSet<Permission>()
            permissions.put(permission.role, rolePerms)
        }
        rolePerms.add(permission)
    }

    public static boolean hasPermission(Integer role, Permission permission){
        Set<Permission> rolePerms = getPermissions(role)
        return rolePerms && rolePerms.contains(permission)
    }

    public static boolean hasPermission(Integer role, String permission){
        permission = permission?.toLowerCase()

        boolean hasPermission = false
        getPermissions(role)?.each(){ Permission perm ->
            if (perm.permission.toLowerCase() == permission)
                hasPermission = true
        }
        return hasPermission
    }

    public static Set<Permission> getPermissions(Integer role){
        final Set<Permission> rolePerms = []

        permissions.collect {Integer uRole, Set perms ->
            if(uRole <= role)
               rolePerms.addAll(perms)
        }

        return rolePerms
    }

    public static Map<String, Set<Permission>> getPermissionsByGroup(Integer role){
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