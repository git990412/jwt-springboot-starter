package com.ll.medium240107.domain.member.member.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.medium240107.domain.member.member.entity.Member;
import com.ll.medium240107.domain.member.member.form.JoinForm;
import com.ll.medium240107.domain.member.member.repository.MemberRepository;
import com.ll.medium240107.domain.member.role.entity.ERole;
import com.ll.medium240107.domain.member.role.entity.Role;
import com.ll.medium240107.domain.member.role.repository.RoleRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public Member join(JoinForm joinForm) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER));

        return memberRepository.save(Member.builder()
                .username(joinForm.getUsername())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .email(joinForm.getEmail())
                .roles(roles)
                .build());
    }
}