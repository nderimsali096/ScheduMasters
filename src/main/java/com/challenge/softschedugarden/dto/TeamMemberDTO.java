package com.challenge.softschedugarden.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamMemberDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private List<String> availabilitySlots;
}
