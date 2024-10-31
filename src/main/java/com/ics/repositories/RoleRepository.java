package com.ics.repositories;

import com.ics.entities.Permission;
import com.ics.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("SELECT r FROM Role r WHERE CONCAT(r.scope, '.role.', r.name) IN :publicNames")
    Set<Role> findByPublicNames(@Param("publicNames") Collection<String> publicNames);

    @Query("SELECT p FROM Role p WHERE CONCAT(p.scope, '.role.', p.name) LIKE %:publicName%")
    Page<Role> findByPublicName(@Param("publicName") String publicName, Pageable pageable);

    @Query("SELECT r FROM Role r WHERE CONCAT(r.scope, '.role.', r.name) = :publicName")
    Optional<Role> findByPublicName(@Param("publicName") String publicName);
}

