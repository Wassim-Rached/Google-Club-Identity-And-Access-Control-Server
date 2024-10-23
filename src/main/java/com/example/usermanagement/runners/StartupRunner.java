package com.example.usermanagement.runners;

import com.example.usermanagement.entities.Permission;
import com.example.usermanagement.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner{

    private final PermissionRepository permissionRepository;

    @Override
    public void run(ApplicationArguments args) {
        // create special permissions

        var permissions = List.of(
                new Permission("special.perm.verified_email"),
                new Permission("special.perm.membership"),
                new Permission("special.perm.verified_identity")
        );

        // check if the permissions already exist
        for (Permission permission : permissions) {
            if (permissionRepository.findByPublicName(permission.getPublicName()).isEmpty()) {
                permissionRepository.save(permission);
            }
        }
    }
}
