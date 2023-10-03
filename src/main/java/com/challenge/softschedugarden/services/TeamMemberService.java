package com.challenge.softschedugarden.services;

import com.challenge.softschedugarden.dto.MeetingResponseDTO;
import com.challenge.softschedugarden.dto.WeeklyAvailabilityDTO;
import com.challenge.softschedugarden.entities.TeamMember;
import com.challenge.softschedugarden.exceptions.InvalidRequestException;
import com.challenge.softschedugarden.exceptions.MeetingValidationException;
import com.challenge.softschedugarden.exceptions.ResourceNotFoundException;
import com.challenge.softschedugarden.interfaces.services.ITeamMemberService;
import com.challenge.softschedugarden.repositories.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class TeamMemberService implements ITeamMemberService {
    @Autowired
    private TeamMemberRepository teamMemberRepository;

    public TeamMember findById(Long id) {
        return getUserOrFail(id, "User with this ID could not be found!", null);
    }

    public TeamMember findByEmail(String email) {
        return getUserOrFail(null, "User with this email could not be found!", email);
    }

    public List<TeamMember> getAllTeamMembers() {
        List<TeamMember> teamMembers = teamMemberRepository.findAll();
        return new ArrayList<>(teamMembers);
    }

    public TeamMember updateTeamMember(Long id, TeamMember teamMember) {
        TeamMember existingTeamMember = getUserOrFail(id, "User with this ID could not be found!", null);

        // Update existingTeamMember fields with values from teamMemberDTO
        existingTeamMember.setName(teamMember.getName());
        // Update other fields as needed

        TeamMember updatedTeamMember = teamMemberRepository.save(existingTeamMember);
        return updatedTeamMember;
    }

    public void deleteTeamMember(Long id) {
        getUserOrFail(id, "User with this ID could not be found!", null);
        teamMemberRepository.deleteById(id);
    }

    public MeetingResponseDTO findOptimalMeetingSlot(WeeklyAvailabilityDTO weeklyAvailability) {
        Map<String, List<String>> availability = weeklyAvailability.getAvailability();

        if (availability.isEmpty()) {
            return new MeetingResponseDTO();
        }

        validateParticipants(availability);
        validateSlotConflicts(availability);

        Map<String, Integer> slotCount = countSlots(availability);

        String optimalSlot = findOptimalSlot(slotCount);

        if (isUniqueSlot(optimalSlot, slotCount)) {
            throw new MeetingValidationException("No optimal meeting slot found due to unique slots.");
        }

        List<String> participants = findParticipantsForSlot(availability, optimalSlot);

        MeetingResponseDTO responseDTO = new MeetingResponseDTO();
        responseDTO.setOptimalSlot(optimalSlot);
        responseDTO.setParticipants(participants);

        return responseDTO;
    }

    public void validateSlotConflicts(Map<String, List<String>> availability) {
        for (Map.Entry<String, List<String>> entry : availability.entrySet()) {
            String participant = entry.getKey();
            List<String> slots = entry.getValue();

            Set<String> checkedDays = new HashSet<>();

            for (String slot : slots) {
                String day = slot.split(" ")[0];
                String timeRange = slot.split(" ")[1];

                if (checkedDays.contains(day)) {
                    throw new MeetingValidationException("Conflict of slots for participant " + participant);
                }

                for (String otherSlot : slots) {
                    if (!slot.equals(otherSlot)) {
                        String otherDay = otherSlot.split(" ")[0];
                        String otherTimeRange = otherSlot.split(" ")[1];
                        if (day.equals(otherDay) && doSlotsOverlap(timeRange, otherTimeRange)) {
                            throw new MeetingValidationException("Conflict of slots for participant " + participant);
                        }
                    }
                }

                checkedDays.add(day);
            }
        }
    }

    private boolean doSlotsOverlap(String slot1, String slot2) {
        String[] parts1 = slot1.split("-");
        String[] parts2 = slot2.split("-");

        // Extract start and end times
        String startTime1 = parts1[0];
        String endTime1 = parts1[1];
        String startTime2 = parts2[0];
        String endTime2 = parts2[1];

        // Check for overlap
        return !(
                LocalTime.parse(endTime1).isBefore(LocalTime.parse(startTime2)) ||
                        LocalTime.parse(endTime2).isBefore(LocalTime.parse(startTime1))
        );
    }


    private void validateParticipants(Map<String, List<String>> availability) {
        if (availability.size() < 2) {
            throw new MeetingValidationException("Not enough participants to conduct a meeting");
        }
    }

    private Map<String, Integer> countSlots(Map<String, List<String>> availability) {
        Map<String, Integer> slotCount = new HashMap<>();

        for (List<String> slots : availability.values()) {
            for (String slot : slots) {
                slotCount.put(slot, slotCount.getOrDefault(slot, 0) + 1);
            }
        }

        return slotCount;
    }

    private String findOptimalSlot(Map<String, Integer> slotCount) {
        String optimalSlot = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : slotCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                optimalSlot = entry.getKey();
            }
        }

        return optimalSlot;
    }

    private boolean isUniqueSlot(String optimalSlot, Map<String, Integer> slotCount) {
        return slotCount.get(optimalSlot) == 1;
    }

    private List<String> findParticipantsForSlot(Map<String, List<String>> availability, String optimalSlot) {
        List<String> participants = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : availability.entrySet()) {
            if (entry.getValue().contains(optimalSlot)) {
                participants.add(entry.getKey());
            }
        }

        return participants;
    }




    private TeamMember getUserOrFail(Long id, String message, String email) {
        Optional<TeamMember> teamMember;

        if (id != null) {
            teamMember = teamMemberRepository.findById(id);
        } else if (email != null) {
            teamMember = teamMemberRepository.findByEmail(email);
        } else {
            throw new InvalidRequestException("Both id and email are null. At least one must be provided.");
        }

        return teamMember.orElseThrow(() -> new ResourceNotFoundException(message));
    }

}
