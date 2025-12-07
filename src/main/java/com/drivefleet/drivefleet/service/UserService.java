package com.drivefleet.drivefleet.service;

import com.drivefleet.drivefleet.domain.dto.user.UserRequest;
import com.drivefleet.drivefleet.domain.dto.user.UserResponse;
import com.drivefleet.drivefleet.domain.entities.User;
import com.drivefleet.drivefleet.domain.enums.UserStatus;
import com.drivefleet.drivefleet.exceptions.EmailAlreadyInUseException;
import com.drivefleet.drivefleet.exceptions.UserHasBeenExcludedException;
import com.drivefleet.drivefleet.exceptions.UserNotFoundEmailException;
import com.drivefleet.drivefleet.exceptions.UserNotFoundIdException;
import com.drivefleet.drivefleet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(UserRequest request) {
        validateEmailAlreadyExists(request.email());

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(newUser);
    }

    @Transactional
    public void update(UUID id, UserRequest request) {
        User user = ensureExists(id);

        ensureActive(user);
        if (!user.getEmail().equals(request.email())) {
            validateEmailAlreadyExists(request.email());
        }

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

    @Transactional
    public void deleteById(UUID id) {
        User user = ensureExists(id);

        ensureActive(user);
        user.setStatus(UserStatus.EXCLUDED);
    }

    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundIdException(id.toString()));

        ensureActive(user);
        return convertToResponse(user);
    }

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundEmailException(email));

        ensureActive(user);
        return convertToResponse(user);
    }

    private void ensureActive(User user) {
        if (user.getStatus() == UserStatus.EXCLUDED) {
            throw new UserHasBeenExcludedException(user.getId().toString());
        }
    }

    private void validateEmailAlreadyExists(String emailToCheck) {
        if (userRepository.existsByEmail(emailToCheck)) {
            throw new EmailAlreadyInUseException(emailToCheck);
        }
    }

    private User ensureExists(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundIdException(id.toString()));
    }

    protected UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
