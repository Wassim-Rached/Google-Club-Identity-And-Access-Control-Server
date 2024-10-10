package com.example.usermanagement.entities;

import com.example.usermanagement.exceptions.InputValidationException;
import jakarta.persistence.*;
import lombok.Data;
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
}
