package com.cmms.app.controller;

import com.cmms.app.dto.base.response.BaseGetAllResponse;
import com.cmms.app.dto.user.request.UserCreateRequest;
import com.cmms.app.dto.user.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cmms.app.dto.base.response.ApiResponse;
import com.cmms.app.dto.user.response.UserResponse;
import com.cmms.app.mapper.UserMapper;
import com.cmms.app.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;


    @GetMapping("{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }

    @GetMapping("getAll")
    public ApiResponse<BaseGetAllResponse<UserResponse>> getAllUsers() {
        return ApiResponse.<BaseGetAllResponse<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("getByUsername")
    public ApiResponse<UserResponse> getUserByUsername(String username) {
        UserResponse userResponse = userService.getUserByUsername(username);
        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }

    @PostMapping("create")
    public ApiResponse<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }

    @PutMapping("update/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id ,@RequestBody UserUpdateRequest request) {
        request.setUserId(id);
        UserResponse userResponse = userService.updateUser(request);
        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }

}
