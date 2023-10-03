package com.challenge.softschedugarden.repositories;

import com.challenge.softschedugarden.entities.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    Optional<TeamMember> findByEmail(String email);
    Boolean existsByEmail(String email);
}

