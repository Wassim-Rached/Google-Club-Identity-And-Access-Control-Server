package com.example.usermanagement.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;


@Entity
@Getter
@Setter
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String photoUrl;

    @Column(nullable = false,name = "is_email_verified",columnDefinition = "boolean default false")
    private Boolean isEmailVerified;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_permissions",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();



    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Add roles as authorities
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getPublicName())));

        // Add permissions from roles
        roles.stream()
                .map(Role::getPermissions)  // Assuming Role class has a getPermissions() method
                .flatMap(Set::stream)
                .map(permission -> new SimpleGrantedAuthority(permission.getPublicName()))
                .forEach(authorities::add);

        // Add individual permissions
        permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPublicName()))
                .forEach(authorities::add);

        return authorities;
    }

//    hashcode
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

//    equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Account account = (Account) obj;
        return email.equals(account.email);
    }

    // toString
    @Override
    public String toString() {
        return email;
    }

    public static boolean isValidEmail(String email) {
        // regex pattern for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static void validateEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address : " + email);
        }
    }

}
