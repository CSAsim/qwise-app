package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.response.UserResponseDto;
import az.company.qwisedemoapp.model.dto.request.UpdateUserRequestDto;
import az.company.qwisedemoapp.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserResponseDto> getUserInfo()    {
        return ResponseEntity.ok(userService.findUserByToken());
    }

    @PutMapping("/update-user")
    public ResponseEntity<UserResponseDto> update(@RequestBody @Valid UpdateUserRequestDto request) {
        return ResponseEntity.ok(userService.updateUser(request));
    }
}


