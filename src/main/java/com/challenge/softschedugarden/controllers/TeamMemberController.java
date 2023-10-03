package com.challenge.softschedugarden.controllers;

import com.challenge.softschedugarden.dto.AvailabilitySlotDTO;
import com.challenge.softschedugarden.dto.TeamMemberDTO;
import com.challenge.softschedugarden.entities.AvailabilitySlot;
import com.challenge.softschedugarden.entities.TeamMember;
import com.challenge.softschedugarden.interfaces.services.IAuthenticationService;
import com.challenge.softschedugarden.interfaces.services.ITeamMemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/team-members")
public class TeamMemberController {
    @Autowired
    private ITeamMemberService teamMemberService;
    @Autowired
    private IAuthenticationService authenticationService;
    @Autowired
    private ModelMapper modelMapper;

    @Operation(
            summary = "Retrieves teamMember by id",
            description = "Finds teamMember by given id and returns it",
            tags = {"Team member"})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<TeamMemberDTO> getTeamMember(@PathVariable Long id) {
        TeamMemberDTO teamMemberDTO = convertToDto(teamMemberService.findById(id));
        if (teamMemberDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teamMemberDTO);
    }

    @Operation(
            summary = "Retrieves teamMember by email",
            description = "Finds teamMember by given email and returns it",
            tags = {"Team member"})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/email")
    public ResponseEntity<TeamMemberDTO> getTeamMemberByEmail(@RequestParam("email") String email) {
        TeamMemberDTO teamMemberDTO = convertToDto(teamMemberService.findByEmail(email));
        if (teamMemberDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teamMemberDTO);
    }

    @Operation(
            summary = "Retrieves all team members",
            description = "Finds all team members and returns them",
            tags = {"Team member"})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/all")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamMembers() {
        List<TeamMember> teamMembers = teamMemberService.getAllTeamMembers();
        List<TeamMemberDTO> teamMembersDTO = convertToDtoList(teamMembers, this::convertToDto);
        return ResponseEntity.ok(teamMembersDTO);
    }

    @Operation(
            summary = "Updates a team member",
            description = "Updates an existing team member with the provided data",
            tags = {"Team member"})
    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<TeamMemberDTO> updateTeamMember(@PathVariable Long id, @RequestBody TeamMemberDTO teamMemberDTO) throws ParseException {
        TeamMember teamMember = convertToEntity(teamMemberDTO);
        TeamMember updatedTeamMember = teamMemberService.updateTeamMember(id, teamMember);
        if (updatedTeamMember == null) {
            return ResponseEntity.notFound().build();
        }
        TeamMemberDTO updatedTeamMemberDto = convertToDto(updatedTeamMember);
        return ResponseEntity.ok(updatedTeamMemberDto);
    }

    @Operation(
            summary = "Deletes a team member",
            description = "Deletes a team member by ID",
            tags = {"Team member"})
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamMember(@PathVariable Long id) {
        teamMemberService.deleteTeamMember(id);
        return ResponseEntity.noContent().build();
    }

    private TeamMemberDTO convertToDto(TeamMember teamMember) {
        return modelMapper.map(teamMember, TeamMemberDTO.class);
    }

    private TeamMember convertToEntity(TeamMemberDTO teamMemberDTO) throws ParseException {
        return modelMapper.map(teamMemberDTO, TeamMember.class);
    }

    private <M, D> List<D> convertToDtoList(List<M> models, Function<M, D> modelToDtoConverter) {
        return models.stream()
                .map(modelToDtoConverter)
                .toList();
    }

}
