package com.challenge.softschedugarden.security;

import com.challenge.softschedugarden.entities.TeamMember;
import com.challenge.softschedugarden.enums.Role;
import com.challenge.softschedugarden.repositories.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public CustomUserDetailsService(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TeamMember teamMember = teamMemberRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("TeamMember with email not found!"));
        return new User(teamMember.getEmail(), teamMember.getPassword(), mapRoleToAuthorities(teamMember.getRole()));
    }

    private Collection<GrantedAuthority> mapRoleToAuthorities(Role role) {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.name())).collect(Collectors.toList());
    }
}
