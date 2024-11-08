package com.ics.entities;

import com.ics.exceptions.InputValidationException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "roles")
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String scope;

    private String description;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private Set<Account> accounts = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();


    public Role(String publicName) {
        String[] parts = publicName.split("\\.");
        if (parts.length != 3) {
            throw new InputValidationException("Invalid role 'publicName' format : " + publicName);
        }
        this.scope = parts[0];
        this.name = parts[2];
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Role role = (Role) obj;

//        if (id == null || role.id == null) {
            return this.getPublicName().equals(role.getPublicName());
//        }

//        return id.equals(role.id);
    }

    @Override
    public String toString() {
        return getPublicName();
    }

    @Override
    public int hashCode() {
        return getPublicName().hashCode();
    }


    public String getPublicName() {
        return scope + ".role." + name;
    }
//
//    public String getLocalName() {
//        return "ROLE_" + name;
//    }

    public static boolean isValidPublicName(String publicName) {
        return publicName.matches("^[a-z_]+\\.role\\.[a-z_]+$");
    }

    public static void validatePublicName(String publicName) {
        if (!isValidPublicName(publicName)) {
            throw new InputValidationException("Role public name must be in the format 'scope.role.name' : " + publicName);
        }
    }

    public static void validateCanBeCreated(String name, String scope) {
        if(name == null || name.isBlank())
            throw new InputValidationException("Role name is required");
        if(scope == null || scope.isBlank())
            throw new InputValidationException("Role scope is required");
        if(!name.matches("[a-zA-Z_]+"))
            throw new InputValidationException("Role name can only contain letters and _ : " + name);
        if(!scope.matches("[a-zA-Z_]+"))
            throw new InputValidationException("Role scope can only contain letters and _ : " + scope);
        if(scope.equals("special")){
            throw new InputValidationException("Role scope cannot be 'special'");
        }
    }

    public boolean isSpecial() {
        return this.scope.equals("special");
    }

    public static boolean isSpecial(String publicName) {
        return getScopeFromPublicName(publicName).equals("special");
    }

    public static String getScopeFromPublicName(String publicName) {
        return publicName.split("\\.")[0];
    }

    public static boolean canBeGrantedPermission(String rolePublicName,String permissionPublicName) {
        // they should either be at the same scope or the role is global
        String roleScope = Role.getScopeFromPublicName(rolePublicName);
        String permissionScope = Permission.getScopeFromPublicName(permissionPublicName);
        return roleScope.equals(permissionScope) || roleScope.equals("global");
    }

    public static boolean canBeRevokedPermission(String rolePublicName, String permissionPublicName) {
        // they should either be at the same scope or the role is global
        String roleScope = Role.getScopeFromPublicName(rolePublicName);
        String permissionScope = Permission.getScopeFromPublicName(permissionPublicName);
        return roleScope.equals(permissionScope) || roleScope.equals("global");
    }

    public static void validateCanBeGrantedPermission(String rolePublicName, String permissionPublicName) {
        if (!canBeGrantedPermission(rolePublicName, permissionPublicName)) {
            throw new InputValidationException("Role " + rolePublicName + " cannot be granted permission " + permissionPublicName);
        }
    }

    public static void validateCanBeRevokedPermission(String rolePublicName, String permissionPublicName) {
        if (!canBeRevokedPermission(rolePublicName, permissionPublicName)) {
            throw new InputValidationException("Role " + rolePublicName + " cannot be revoked permission " + permissionPublicName);
        }
    }

}
