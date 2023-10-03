package com.challenge.softschedugarden.interfaces.services;

import com.challenge.softschedugarden.dto.MeetingResponseDTO;
import com.challenge.softschedugarden.dto.WeeklyAvailabilityDTO;
import com.challenge.softschedugarden.entities.AvailabilitySlot;
import com.challenge.softschedugarden.entities.TeamMember;

import java.util.List;

public interface ITeamMemberService {
    TeamMember findById(Long id);
    TeamMember findByEmail(String email);
    List<TeamMember> getAllTeamMembers();
    TeamMember updateTeamMember(Long id, TeamMember teamMember);
    void deleteTeamMember(Long id);
    MeetingResponseDTO findOptimalMeetingSlot(WeeklyAvailabilityDTO weeklyAvailability);
}
