package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.request.test.StartAttemptRequestDto;
import az.company.qwisedemoapp.model.dto.response.attempt.ResumeAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.response.attempt.StartAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.response.attempt.ResultAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.request.test.AttemptRequestDto;
import az.company.qwisedemoapp.service.test.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/user/attempt")
public class UserPacketAttemptController{

    private final AttemptService attemptService;

    @PostMapping("/start-attempt")
    public ResponseEntity<StartAttemptResponseDto> startAttempt(@RequestBody StartAttemptRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attemptService.startAttempt(request));
    }

    @PostMapping("/pause-attempt")
    public ResponseEntity<ResultAttemptResponseDto> pauseAttempt(@RequestBody AttemptRequestDto request) {
        return ResponseEntity.ok(attemptService.pauseAttempt(request));
    }

    @PostMapping("/resume-attempt/{attemptId}")
    public ResponseEntity<ResumeAttemptResponseDto> resumeAttempt(@PathVariable("attemptId") Long attemptId) {
        return ResponseEntity.ok(attemptService.resumeAttempt(attemptId));
    }

    @PostMapping("/finish-attempt")
    public ResponseEntity<ResultAttemptResponseDto> finishAttempt(@RequestBody AttemptRequestDto request) {
        return ResponseEntity.ok(attemptService.finishAttempt(request));
    }
}
