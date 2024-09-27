package com.example.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String scope;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Account> accounts;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Permission permission = (Permission) obj;
        return id.equals(permission.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

//    public String getPublicName() {
//        return scope + ".perm." + name;
//    }
//
//    public String getLocalName() {
//        return name;
//    }
}
