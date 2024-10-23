package com.example.usermanagement.entities;

import com.example.usermanagement.exceptions.InputValidationException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "permissions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String scope;

    private String description;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Account> accounts = new HashSet<>();

    public Permission(String publicName) {
        String[] parts = publicName.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid permission name: " + publicName);
        }
        scope = parts[0];
        name = parts[2];
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Permission permission = (Permission) obj;

//        if (id == null || permission.id == null) {
        return getPublicName().equals(permission.getPublicName());
//        }

//        return id.equals(permission.id);
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
        return scope + ".perm." + name;
    }
//
//    public String getLocalName() {
//        return name;
//    }

    public static boolean isValidPublicName(String publicName) {
        return publicName.matches("^[a-z_]+\\.perm\\.[a-z_]+$");
    }

    public static void validatePublicName(String publicName) {
        if (!isValidPublicName(publicName)) {
            throw new InputValidationException("Permission public name must be in the format 'scope.perm.name' : " + publicName);
        }
    }

    public boolean isSpecial() {
        return scope.equals("special");
    }

    public static boolean isSpecial(String publicName){
        return Permission.getScopeFromPublicName(publicName).equals("special");
    }

    public static String getScopeFromPublicName(String publicName) {
        return publicName.split("\\.")[0];
    }
}
