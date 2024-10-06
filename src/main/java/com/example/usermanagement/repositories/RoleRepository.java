package com.example.usermanagement.repositories;

import com.example.usermanagement.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("SELECT r FROM Role r WHERE CONCAT(r.scope, '.role.', r.name) IN :publicNames")
    List<Role> findByPublicNames(@Param("publicNames") List<String> publicNames);
}

