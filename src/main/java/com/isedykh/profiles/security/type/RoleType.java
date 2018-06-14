package com.isedykh.profiles.security.type;

public enum RoleType {

    ROLE_ADMIN;

    public static RoleType findByType(String type) {
        for(RoleType role: values()) {
            if(role.name().equals(type)) {
                return role;
            }
        }
        return null;
    }
}
