package com.time_tracker.be.controller;

import com.time_tracker.be.annotation.CurrentUser;
import com.time_tracker.be.dto.CurrentUserDto;
import com.time_tracker.be.dto.TimeSessionDto;
import com.time_tracker.be.model.ResponseModel;
import com.time_tracker.be.model.TimeSessionModel;
import com.time_tracker.be.service.TimeSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/1.0/time-sessions")
public class TimeSessionController {
    private final TimeSessionService timeSessionService;

    public TimeSessionController(TimeSessionService timeSessionService) {
        this.timeSessionService = timeSessionService;
    }

    // Define your endpoints here
    @GetMapping
    public ResponseEntity<ResponseModel<List<TimeSessionDto>>> getAllTimeSessions(@CurrentUser CurrentUserDto currentUser) {
        Long userId = currentUser.getId_user();
        log.info("Get all time sessions for user: {}", userId);
        return timeSessionService.getAllTimeSessions(userId);
    }
    @GetMapping("/active")
    public ResponseEntity<ResponseModel<TimeSessionDto>> getActiveTimeSession(@CurrentUser CurrentUserDto currentUser) {
        Long userId = currentUser.getId_user();
        log.info("Get active time session for user: {}", userId);
        return timeSessionService.getActiveTimeSession(userId);
    }

    @PostMapping("/start")
    public ResponseEntity<ResponseModel<TimeSessionDto>> createTimeSession(@CurrentUser CurrentUserDto currentUser) {
        log.info("Create time session");
        log.info("Current user: {}", currentUser);
        Long userId = currentUser.getId_user();
        return timeSessionService.startTimeSession(userId);
    }

    @PutMapping("/stop")
    public ResponseEntity<ResponseModel<TimeSessionDto>> stopTimeSession(@CurrentUser CurrentUserDto currentUser) {
        Long userId = currentUser.getId_user();
        return timeSessionService.stopTimeSession(userId);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ResponseModel<TimeSessionDto>> updateTimeSession(@CurrentUser CurrentUserDto currentUser, @PathVariable Long id, @RequestBody TimeSessionModel timeSessionModel) {
        Long userId = currentUser.getId_user();
        return timeSessionService.updateTimeSession(userId, id, timeSessionModel);
    }

}
