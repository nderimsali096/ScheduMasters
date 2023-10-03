package com.challenge.softschedugarden.entities;

import com.challenge.softschedugarden.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_members")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;

    @Enumerated(EnumType.STRING )
    private Role role;

    @ElementCollection
    @CollectionTable(name = "availability_slots", joinColumns = @JoinColumn(name = "team_member_id"))
    @Column(name = "slot")
    private List<String> availabilitySlots;
}
