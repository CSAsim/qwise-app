package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.request.test.UserPacketAttemptRequestDto;
import az.company.qwisedemoapp.model.dto.request.test.UserPacketAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.request.test.UserPacketFinishAttemptRequestDto;
import az.company.qwisedemoapp.model.dto.response.test.answer.UserAnswerResponseDto;
import az.company.qwisedemoapp.service.test.UserAnswerService;
import az.company.qwisedemoapp.service.test.UserPacketAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/user/attempt")
public class UserPacketAttemptController{

    private final UserPacketAttemptService attemptService;

    @PostMapping("/start-attempt")
    public ResponseEntity<UserPacketAttemptResponseDto> startAttempt(@RequestBody UserPacketAttemptRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attemptService.startAttempt(request));
    }

    @PostMapping("/finish-attempt")
    public ResponseEntity<UserPacketAttemptResponseDto> finishAttempt(@RequestBody UserPacketFinishAttemptRequestDto request) {
        return ResponseEntity.ok(attemptService.finishAttempt(request));
    }
}
