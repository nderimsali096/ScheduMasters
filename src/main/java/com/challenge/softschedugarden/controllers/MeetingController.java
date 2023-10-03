package com.challenge.softschedugarden.controllers;

import com.challenge.softschedugarden.dto.MeetingResponseDTO;
import com.challenge.softschedugarden.dto.WeeklyAvailabilityDTO;
import com.challenge.softschedugarden.services.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    @Autowired
    private TeamMemberService teamMemberService;

    @PostMapping("/find-optimal-slot")
    public ResponseEntity<MeetingResponseDTO> findOptimalMeetingSlot(@RequestBody WeeklyAvailabilityDTO weeklyAvailability) {
        MeetingResponseDTO responseDTO = teamMemberService.findOptimalMeetingSlot(weeklyAvailability);
        return ResponseEntity.ok(responseDTO);
    }
}

