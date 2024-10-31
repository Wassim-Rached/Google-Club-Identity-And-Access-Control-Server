package com.ics.runners;

import com.ics.entities.Permission;
import com.ics.repositories.PermissionRepository;
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
                Permission.builder()
                        .scope("special")
                        .name("perm.verified_email")
                        .description("This permission is granted to accounts with verified email").build(),
                Permission.builder()
                        .scope("special")
                        .name("perm.membership")
                        .description("This permission is granted to accounts with membership").build(),
                Permission.builder()
                        .scope("special")
                        .name("perm.verified_identity")
                        .description("This permission is granted to accounts with verified identity").build()
        );

        // check if the permissions already exist
        for (Permission permission : permissions) {
            if (permissionRepository.findByPublicName(permission.getPublicName()).isEmpty()) {
                permissionRepository.save(permission);
            }
        }
    }
}
