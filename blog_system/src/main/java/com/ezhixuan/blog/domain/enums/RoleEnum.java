package com.ezhixuan.blog.domain.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ROLE_USER("user", "普通用户"),
    ROLE_ADMIN("admin", "管理员");

    private final String role;
    private final String roleName;

    RoleEnum(String role, String roleName) {
        this.role = role;
        this.roleName = roleName;
    }

    public static RoleEnum getByRole(String role) {
        for (RoleEnum value : RoleEnum.values()) {
            if (value.getRole().equals(role)) {
                return value;
            }
        }
        return null;
    }
}
