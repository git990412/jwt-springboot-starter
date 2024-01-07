package com.ll.medium240107.global.initData;

import java.util.stream.Stream;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.medium240107.domain.member.role.entity.ERole;
import com.ll.medium240107.domain.member.role.entity.Role;
import com.ll.medium240107.domain.member.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitRoles implements ApplicationRunner {
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (roleRepository.count() == 0) {
            Stream.of(ERole.values())
                    .forEach(role -> {
                        Role newRole = new Role();
                        newRole.setName(role);
                        roleRepository.save(newRole);
                    });
        }
    }
}