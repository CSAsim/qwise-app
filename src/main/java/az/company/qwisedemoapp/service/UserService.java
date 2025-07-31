package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.mapper.UserMapper;
import az.company.qwisedemoapp.model.dto.UserResponse;
import az.company.qwisedemoapp.model.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserService::notFound);
        return userMapper.toResponse(user);
    }

    public UserResponse findById(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserService::notFound);
        return userMapper.toResponse(user);
    }

    public UserResponse findUserByToken() {
        return userMapper.toResponse(getEntity());
    }

    @Transactional
    public UserResponse updateUser(UpdateUserRequest request) {
        User entity = getEntity();
        entity.setFullName(request.getFullName());
        User savedEntity = userRepository.save(entity);
        return userMapper.toResponse(savedEntity);
    }

    private static NotFoundException notFound() {
        return new NotFoundException("User not found");
    }

    private User getEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(UserService::notFound);
    }
}
