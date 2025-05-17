package com.time_tracker.be.service;

import com.time_tracker.be.dto.TimeSessionDto;
import com.time_tracker.be.exception.BadRequestException;
import com.time_tracker.be.exception.ForbiddenException;
import com.time_tracker.be.exception.NotAuthorizedException;
import com.time_tracker.be.exception.NotFoundException;
import com.time_tracker.be.model.ResponseModel;
import com.time_tracker.be.model.TimeSessionModel;
import com.time_tracker.be.model.UserModel;
import com.time_tracker.be.repository.TimeSessionRepository;
import com.time_tracker.be.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
public class TimeSessionService {

    // This service will handle the business logic for time sessions
    // You can inject repositories here to interact with the database
    private final TimeSessionRepository timeSessionRepository;
    private final UserRepository userRepository;

    public TimeSessionService(TimeSessionRepository timeSessionRepository, UserRepository userRepository) {
        this.timeSessionRepository = timeSessionRepository;
        this.userRepository = userRepository;
    }

    // Example method to get all time sessions
    public ResponseEntity<ResponseModel<List<TimeSessionDto>>> getAllTimeSessions(Long userId) {
        // Fetch time sessions for the user
        UserModel userModel = new UserModel();
        userModel.setId_user(userId);
        List<TimeSessionModel> timeSessionModels = timeSessionRepository.findByIdUser(userModel);

        List<TimeSessionDto> timeSessionDtos = timeSessionModels.stream()
                .map(timeSessionModel -> TimeSessionDto.builder()
                        .idUser(userId)
                        .idTimeSession(timeSessionModel.getIdTimeSession())
                        .startTime(timeSessionModel.getStartTime())
                        .endTime(timeSessionModel.getEndTime())
                        .description(timeSessionModel.getDescription())
                        .build())
                .parallel()
                .toList();

        ResponseModel<List<TimeSessionDto>> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
        responseModel.setMessage("Time sessions retrieved successfully");
        responseModel.setData(timeSessionDtos);
        return ResponseEntity.ok(responseModel);
    }

    public ResponseEntity<ResponseModel<TimeSessionDto>> getActiveTimeSession(Long userId) {
        // Fetch the latest time session for the user
        UserModel userModel = new UserModel();
        userModel.setId_user(userId);
        TimeSessionModel timeSessionModel = timeSessionRepository.findLatestByIdUser(userModel);
        if (timeSessionModel == null || timeSessionModel.getEndTime() != null) {
            throw new NotFoundException("No active time session found");
        }

        TimeSessionDto timeSessionDto = TimeSessionDto.builder()
                .idUser(userId)
                .startTime(timeSessionModel.getStartTime())
                .endTime(timeSessionModel.getEndTime())
                .idTimeSession(timeSessionModel.getIdTimeSession())
                .build();

        ResponseModel<TimeSessionDto> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
        responseModel.setMessage("Active time session retrieved successfully");
        responseModel.setData(timeSessionDto);
        return ResponseEntity.ok(responseModel);
    }

    // Example method to create a new time session
    public ResponseEntity<ResponseModel<TimeSessionDto>> startTimeSession(Long userId) {
        TimeSessionModel timeSessionModel = new TimeSessionModel();
        timeSessionModel.setIdUser(userRepository.findById(userId).orElse(null));
        // Assuming you have a method in your repository to find time sessions by user ID
        if (timeSessionModel.getIdUser() == null) {
            log.info(timeSessionModel.toString());
            throw new NotAuthorizedException("User not found");
        }

        TimeSessionModel isExistingSession = timeSessionRepository.findLatestByIdUser(timeSessionModel.getIdUser());
        if (isExistingSession != null && isExistingSession.getEndTime() == null) {
            throw new BadRequestException("User already has an active time session");
        }

        // Set the start time to the current time
        timeSessionModel.setStartTime(ZonedDateTime.now(ZoneId.of("Asia/Jakarta")));

        TimeSessionModel savedTimeSessionModel = timeSessionRepository.save(timeSessionModel);

        TimeSessionDto timeSessionDto = TimeSessionDto.builder()
                .idUser(userId)
                .startTime(savedTimeSessionModel.getStartTime())
                .idTimeSession(savedTimeSessionModel.getIdTimeSession())
                .build();

        ResponseModel<TimeSessionDto> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
        responseModel.setMessage("Time session created successfully");
        responseModel.setData(timeSessionDto);
        return ResponseEntity.status(201).body(responseModel);
    }

    // Example method to stop a time session
    public ResponseEntity<ResponseModel<TimeSessionDto>> stopTimeSession(Long userId) {
        // Fetch the latest time session for the user
        UserModel userModel = new UserModel();
        userModel.setId_user(userId);
        TimeSessionModel timeSessionModel = timeSessionRepository.findLatestByIdUser(userModel);
        if (timeSessionModel == null || timeSessionModel.getEndTime() != null) {
            throw new BadRequestException("No active time session found");
        }
        // Set the end time to the current time
        timeSessionModel.setEndTime(ZonedDateTime.now(ZoneId.of("Asia/Jakarta")));

        TimeSessionModel updatedTimeSessionModel = timeSessionRepository.save(timeSessionModel);

        TimeSessionDto timeSessionDto = TimeSessionDto.builder()
                .idUser(userId)
                .startTime(updatedTimeSessionModel.getStartTime())
                .endTime(updatedTimeSessionModel.getEndTime())
                .idTimeSession(updatedTimeSessionModel.getIdTimeSession())
                .build();

        ResponseModel<TimeSessionDto> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
        responseModel.setMessage("Time session stopped successfully");
        responseModel.setData(timeSessionDto);
        return ResponseEntity.ok(responseModel);
    }

    // Example method to update a time session
    public ResponseEntity<ResponseModel<TimeSessionDto>>    updateTimeSession(Long userId, Long id, TimeSessionModel timeSessionModel) {
        // Fetch the time session by ID
        TimeSessionModel existingTimeSession = timeSessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Time session not found"));

        // Check if the user is authorized to update this time session
        if (!existingTimeSession.getIdUser().getId_user().equals(userId)) {
            throw new ForbiddenException("User not authorized to update this time session");
        }

        // Update the time session details
        existingTimeSession.setDescription(timeSessionModel.getDescription());

        TimeSessionModel updatedTimeSession = timeSessionRepository.save(existingTimeSession);

        TimeSessionDto timeSessionDto = TimeSessionDto.builder()
                .idUser(userId)
                .startTime(updatedTimeSession.getStartTime())
                .endTime(updatedTimeSession.getEndTime())
                .idTimeSession(updatedTimeSession.getIdTimeSession())
                .build();

        ResponseModel<TimeSessionDto> responseModel = new ResponseModel<>();
        responseModel.setSuccess(true);
        responseModel.setMessage("Time session updated successfully");
        responseModel.setData(timeSessionDto);
        return ResponseEntity.ok(responseModel);
    }
}
