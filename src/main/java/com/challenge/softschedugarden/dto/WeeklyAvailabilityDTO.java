package com.challenge.softschedugarden.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WeeklyAvailabilityDTO {
    private Map<String, List<String>> availability;
}

