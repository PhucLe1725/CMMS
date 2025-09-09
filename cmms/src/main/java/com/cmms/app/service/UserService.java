package com.cmms.app.service;

import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.dto.user.request.UserCreateRequest;
import com.cmms.app.dto.user.request.UserUpdateRequest;
import com.cmms.app.dto.user.response.UserResponse;
import com.cmms.app.entity.User;
import com.cmms.app.mapper.UserMapper;
import com.cmms.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserResponse getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public BaseGetAllResponse<UserResponse> getAllUsers() {
        List<UserResponse> userResponses = userRepository.findAllUsers()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();

        return BaseGetAllResponse.<UserResponse>builder()
                .data(userResponses)
                .build();
    }

    public UserResponse createUser(UserCreateRequest request) {
        User user = userMapper.toUser(request);
        user.setPasswordHash(new BCryptPasswordEncoder().encode(request.getPassword()));
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(UserUpdateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

}
