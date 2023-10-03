package com.challenge.softschedugarden.services.TeamMemberServiceTest;

import static org.junit.jupiter.api.Assertions.*;

        import com.challenge.softschedugarden.dto.MeetingResponseDTO;
        import com.challenge.softschedugarden.dto.WeeklyAvailabilityDTO;
        import com.challenge.softschedugarden.exceptions.MeetingValidationException;
        import com.challenge.softschedugarden.repositories.TeamMemberRepository;
import com.challenge.softschedugarden.services.TeamMemberService;
import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.MockitoAnnotations;
        import java.util.*;

public class FindOptimalMeetingSlotTest {

    @InjectMocks
    private TeamMemberService teamMemberService;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindOptimalMeetingSlotWithNoParticipants() {
        // Arrange
        WeeklyAvailabilityDTO weeklyAvailability = new WeeklyAvailabilityDTO();
        weeklyAvailability.setAvailability(new HashMap<>()); // Initialize an empty map

        // Act
        MeetingResponseDTO result = teamMemberService.findOptimalMeetingSlot(weeklyAvailability);

        // Assert
        assertNull(result.getOptimalSlot());
        assertNull(result.getParticipants());
    }


    @Test
    public void testFindOptimalMeetingSlotWithSingleParticipant() {
        // Arrange
        WeeklyAvailabilityDTO weeklyAvailability = new WeeklyAvailabilityDTO();
        Map<String, List<String>> availability = new HashMap<>();
        availability.put("Alice", Arrays.asList("Monday 14:00-16:00", "Tuesday 09:00-11:00"));
        weeklyAvailability.setAvailability(availability);

        try {
            // Act
            teamMemberService.findOptimalMeetingSlot(weeklyAvailability);

            // Fail the test if no exception is thrown
            fail("Expected MeetingValidationException");
        } catch (MeetingValidationException ex) {
            // Assert
            assertEquals("Not enough participants to conduct a meeting", ex.getMessage());
        }
    }


    @Test
    public void testFindOptimalMeetingSlotWithMultipleParticipants() {
        // Arrange
        WeeklyAvailabilityDTO weeklyAvailability = new WeeklyAvailabilityDTO();
        Map<String, List<String>> availability = new HashMap<>();
        availability.put("Alice", Arrays.asList("Monday 14:00-16:00", "Tuesday 09:00-11:00"));
        availability.put("Bob", Arrays.asList("Monday 14:00-16:00", "Wednesday 10:00-12:00"));
        availability.put("Charlie", Arrays.asList("Tuesday 08:00-10:00", "Wednesday 11:00-13:00"));
        weeklyAvailability.setAvailability(availability);

        // Act
        MeetingResponseDTO result = teamMemberService.findOptimalMeetingSlot(weeklyAvailability);

        // Assert
        assertNotNull(result);
        assertEquals("Monday 14:00-16:00", result.getOptimalSlot());
        assertEquals(Arrays.asList("Bob", "Alice"), result.getParticipants());
    }

    @Test
    public void testFindOptimalMeetingSlotWithNoCommonSlots() {
        // Arrange
        WeeklyAvailabilityDTO weeklyAvailability = new WeeklyAvailabilityDTO();
        Map<String, List<String>> availability = new HashMap<>();
        availability.put("Alice", Arrays.asList("Monday 14:00-16:00", "Tuesday 09:00-11:00"));
        availability.put("Bob", Arrays.asList("Wednesday 14:00-16:00", "Thursday 10:00-12:00"));
        weeklyAvailability.setAvailability(availability);

        try {
            // Act
            teamMemberService.findOptimalMeetingSlot(weeklyAvailability);

            // Fail the test if no exception is thrown
            fail("Expected MeetingValidationException");
        } catch (MeetingValidationException ex) {
            // Assert
            assertEquals("No optimal meeting slot found due to unique slots.", ex.getMessage());
        }
    }

    @Test
    public void testValidateSlotConflictsWithConflict() {
        // Arrange
        TeamMemberService teamMemberService = new TeamMemberService();
        Map<String, List<String>> availability = new HashMap<>();
        availability.put("Adrian", List.of("Monday 14:00-16:00", "Tuesday 09:00-11:00"));
        availability.put("Johanna", List.of("Monday 11:00-13:00", "Monday 12:00-14:00"));

        try {
            // Act
            teamMemberService.validateSlotConflicts(availability);

            // Fail the test if no exception is thrown
            fail("Expected MeetingValidationException");
        } catch (MeetingValidationException ex) {
            // Assert
            assertEquals("Conflict of slots for participant Johanna", ex.getMessage());
        }
    }

}
