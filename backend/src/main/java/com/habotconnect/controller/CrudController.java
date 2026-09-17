package com.habotconnect.controller;

import com.habotconnect.dto.*;
import com.habotconnect.entity.AuditLog;
import com.habotconnect.service.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Thin HTTP layer only - all business logic (lookups, validation of
 * relations, audit logging) lives in the service package.
 */
@RestController
@RequestMapping("/api")
public class CrudController {

    private final StudentService studentService;
    private final ParentService parentService;
    private final LsaProfileService lsaProfileService;
    private final AvailabilityService availabilityService;
    private final MatchingRequestService matchingRequestService;
    private final MatchService matchService;
    private final SessionService sessionService;
    private final UserService userService;
    private final AuditLogService auditLogService;

    public CrudController(
            StudentService studentService,
            ParentService parentService,
            LsaProfileService lsaProfileService,
            AvailabilityService availabilityService,
            MatchingRequestService matchingRequestService,
            MatchService matchService,
            SessionService sessionService,
            UserService userService,
            AuditLogService auditLogService) {

        this.studentService = studentService;
        this.parentService = parentService;
        this.lsaProfileService = lsaProfileService;
        this.availabilityService = availabilityService;
        this.matchingRequestService = matchingRequestService;
        this.matchService = matchService;
        this.sessionService = sessionService;
        this.userService = userService;
        this.auditLogService = auditLogService;
    }

    // ===================== STUDENTS =====================

    @GetMapping("/students")
    public List<StudentDTO> students() {
        return studentService.list();
    }

    @PostMapping("/students")
    public StudentDTO addStudent(@Valid @RequestBody StudentDTO dto) {
        return studentService.create(dto);
    }

    @PutMapping("/students/{id}")
    public StudentDTO editStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO dto) {
        return studentService.update(id, dto);
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
    }

    // ===================== PARENTS =====================

    @GetMapping("/parents")
    public List<ParentDTO> parents() {
        return parentService.list();
    }

    @PostMapping("/parents")
    public ParentDTO addParent(@Valid @RequestBody ParentDTO dto) {
        return parentService.create(dto);
    }

    // ===================== LSA PROFILES =====================

    @GetMapping("/lsa")
    public List<LsaProfileDTO> lsa() {
        return lsaProfileService.list();
    }

    @PostMapping("/lsa")
    public LsaProfileDTO addLsa(@Valid @RequestBody LsaProfileDTO dto) {
        return lsaProfileService.create(dto);
    }

    @PutMapping("/lsa/{id}")
    public LsaProfileDTO editLsa(@PathVariable Long id, @Valid @RequestBody LsaProfileDTO dto) {
        return lsaProfileService.update(id, dto);
    }

    // ===================== AVAILABILITY =====================

    @GetMapping("/availability")
    public List<AvailabilityDTO> availability() {
        return availabilityService.list();
    }

    @PostMapping("/availability")
    public AvailabilityDTO addAvailability(@Valid @RequestBody AvailabilityDTO dto) {
        return availabilityService.create(dto);
    }

    // ===================== MATCHING REQUESTS =====================

    @GetMapping("/match-requests")
    public List<MatchingRequestDTO> matchRequests() {
        return matchingRequestService.list();
    }

    @PostMapping("/match-requests")
    public MatchingRequestDTO addMatchRequest(@Valid @RequestBody MatchingRequestDTO dto) {
        return matchingRequestService.create(dto);
    }

    @PutMapping("/match-requests/{id}")
    public MatchingRequestDTO updateMatchRequest(@PathVariable Long id, @Valid @RequestBody MatchingRequestDTO dto) {
        return matchingRequestService.update(id, dto);
    }

    // ===================== MATCHES =====================

    @GetMapping("/matches")
    public List<MatchDTO> matches() {
        return matchService.list();
    }

    @PostMapping("/matches")
    public MatchDTO addMatch(@Valid @RequestBody MatchDTO dto) {
        return matchService.create(dto);
    }

    // ===================== SESSIONS =====================

    @GetMapping("/sessions")
    public List<SessionDTO> sessions() {
        return sessionService.list();
    }

    @PostMapping("/sessions")
    public SessionDTO addSession(@Valid @RequestBody SessionDTO dto) {
        return sessionService.create(dto);
    }

    @PutMapping("/sessions/{id}")
    public SessionDTO editSession(@PathVariable Long id, @Valid @RequestBody SessionDTO dto) {
        return sessionService.update(id, dto);
    }

    // ===================== USERS / AUDIT / DASHBOARD =====================

    @GetMapping("/users")
    public List<UserResponse> users() {
        return userService.list();
    }

    @GetMapping("/audit-logs")
    public List<AuditLog> auditLogs() {
        return auditLogService.list();
    }

    @GetMapping("/dashboard")
    public Map<String, Long> dashboard() {
        return userService.dashboard();
    }
}
