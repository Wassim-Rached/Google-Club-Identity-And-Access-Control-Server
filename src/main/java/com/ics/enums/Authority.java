package com.ics.enums;

import lombok.Getter;

/**
 * Enum representing various authorities for authorization within the application.
 *
 * This enum is critical for managing access control throughout the system.
 * Each constant corresponds to a specific authority that can be checked against
 * user roles and authorities.
 *
 * The authorities defined here are utilized by:
 * - Security guards to verify if a user possesses the required permissions.
 * - The 'api/authorities' endpoint, which exposes the available authorities
 *   to the client for functionality and user interface decisions.
 *
 * Authorities are grouped based on their functionality:
 * - General Authorities: Permissions that apply to various aspects of the application.
 * - Account-related Authorities: Permissions specific to account management operations.
 * - Role-related Authorities: Permissions concerning the management of user roles.
 * - Permission-related Authorities: Permissions for handling application permissions.
 *
 * Usage:
 * - To check if a user has a specific authority, use:
 *  `@PreAuthorize("@securityService.hasAuthority(T(com.ics.enums.Authority).{AUTHORITY_NAME})")
 */
@Getter
public enum Authority {
    // General authorities
    EDIT_AUTHORITIES("ics.perm.edit_authorities"),

    // Account related authorities
    VIEW_ACCOUNT("ics.perm.view_account"),
    SEARCH_ACCOUNTS("ics.perm.search_accounts"),
    LOCK_ACCOUNT("ics.perm.lock_account"),
    CHANGE_MEMBERSHIP("ics.perm.change_membership"),
    VERIFY_IDENTITY("ics.perm.verify_identity"),

    // Role related authorities
    VIEW_ROLE("ics.perm.view_role"),
    SEARCH_ROLES("ics.perm.search_roles"),
    CREATE_ROLE("ics.perm.create_role"),
    DELETE_ROLE("ics.perm.delete_role"),

    // Permission related authorities
    VIEW_PERMISSION("ics.perm.view_permission"),
    SEARCH_PERMISSIONS("ics.perm.search_permissions"),
    CREATE_PERMISSION("ics.perm.create_permission"),
    DELETE_PERMISSION("ics.perm.delete_permission");

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }
}
