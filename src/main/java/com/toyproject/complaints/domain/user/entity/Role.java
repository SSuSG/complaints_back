package com.toyproject.complaints.domain.user.entity;

public enum Role {

    ADMIN(ROLES.ADMIN , "슈퍼관리자"),
    USER(ROLES.USER , "회원");

    public static class ROLES{
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

    private String role;
    private String description;

    Role(String role, String description) {
        this.role = role;
        this.description = description;
    }

    public String getRole() {
        return role;
    }
}
