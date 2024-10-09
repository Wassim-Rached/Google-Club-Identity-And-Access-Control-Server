package com.example.usermanagement.repositories;

import com.example.usermanagement.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.email LIKE %:email%")
    Page<Account> findByEmail(String email, Pageable pageable);

    @Query("SELECT a FROM Account a WHERE a.email IN :emails")
    List<Account> findByEmails(List<String> emails);
//    @Modifying
//    @Transactional
//    @Query(value = """
//DO $$
//BEGIN
//
//    INSERT INTO account_roles (account_id, role_id)
//    SELECT 'a247af23-adf3-4aad-8972-39b2bae78781', id
//    FROM roles
//    WHERE name IN (:addRoles)
//    ON CONFLICT DO NOTHING;
//
//END $$""", nativeQuery = true)
//    void updateAuthorities(
//            @Param("addRoles") List<String> addRoles
//    );

//    DELETE FROM account_roles
//    WHERE account_id = :accountId
//    AND role_id IN (SELECT id FROM roles WHERE name IN (:removeRoles));
//
//    DELETE FROM account_permissions
//    WHERE account_id = :accountId
//    AND permission_id IN (SELECT id FROM permissions WHERE name IN (:removePermissions));


//    INSERT INTO account_roles (account_id, role_id)
//    SELECT :accountId, id
//    FROM roles
//    WHERE name IN (:addRoles)
//    ON CONFLICT DO NOTHING;
//
//    INSERT INTO account_permissions (account_id, permission_id)
//    SELECT :accountId, id
//    FROM permissions
//    WHERE name IN (:addPermissions)
//    ON CONFLICT DO NOTHING;

//    @Modifying
//    @Transactional
//    @Query(value = "DO $$ BEGIN " +
//            "DELETE FROM account_roles WHERE account_id = :accountId AND role_id IN " +
//            "(SELECT id FROM roles WHERE name IN (:removeRoles)); " +
//
//            "INSERT INTO account_roles (account_id, role_id) " +
//            "SELECT :accountId, id FROM roles WHERE name IN (:addRoles) " +
//            "ON CONFLICT DO NOTHING; " +
//
//            "DELETE FROM account_permissions WHERE account_id = :accountId AND permission_id IN " +
//            "(SELECT id FROM permissions WHERE name IN (:removePermissions)); " +
//
//            "INSERT INTO account_permissions (account_id, permission_id) " +
//            "SELECT :accountId, id FROM permissions WHERE name IN (:addPermissions) " +
//            "ON CONFLICT DO NOTHING; " +
//            "END $$", nativeQuery = true)
//    void updateAuthorities(
//            @Param("accountId") String accountId,
//            @Param("addRoles") List<String> addRoles,
//            @Param("removeRoles") List<String> removeRoles,
//            @Param("addPermissions") List<String> addPermissions,
//            @Param("removePermissions") List<String> removePermissions
//    );
}
