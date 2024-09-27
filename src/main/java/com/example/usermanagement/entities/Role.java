package com.example.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String scope;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private Set<Account> accounts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Role role = (Role) obj;
        return id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


//    public String getPublicName() {
//        return scope + ".role." + name;
//    }
//
//    public String getLocalName() {
//        return "ROLE_" + name;
//    }
}
