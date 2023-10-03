package com.challenge.softschedugarden.dto;

import lombok.Data;

import java.util.List;

@Data
public class MeetingResponseDTO {
    private String optimalSlot;
    private List<String> participants;
}

