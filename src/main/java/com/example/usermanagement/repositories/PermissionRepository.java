package com.example.usermanagement.repositories;

import com.example.usermanagement.entities.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    @Query("SELECT p FROM Permission p WHERE CONCAT(p.scope, '.perm.', p.name) IN :publicNames")
    Set<Permission> findByPublicNames(@Param("publicNames") Collection<String> publicNames);

    @Query("SELECT p FROM Permission p WHERE CONCAT(p.scope, '.perm.', p.name) LIKE %:publicName%")
    Page<Permission> findByPublicName(@Param("publicName") String publicName, Pageable pageable);

    @Query("SELECT p FROM Permission p WHERE CONCAT(p.scope, '.perm.', p.name) = :publicName")
    Optional<Permission> findByPublicName(@Param("publicName") String publicName);
}