package com.mahesh.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mahesh.Utils;
import com.mahesh.dto.CreateUserRequest;
import com.mahesh.dto.GetUserResponse;
import com.mahesh.model.User;
import com.mahesh.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public void createUser(@RequestBody @Valid CreateUserRequest createUserRequest) throws JsonProcessingException {

        userService.create(Utils.convertUserCreateRequest(createUserRequest));

    }

    @GetMapping("/user/{userId}")
    public GetUserResponse getUser(@PathVariable("userId") Integer userId) throws Exception {
        User user=userService.get(userId);
        return Utils.convertToUserResponse(user);
    }

    @GetMapping("/user/phone/{phone}")
    public GetUserResponse getUserByPhone(@PathVariable("phone") String phone) throws Exception {
        User user=userService.getByPhone(phone);
        return Utils.convertToUserResponse(user);
    }

//    @GetMapping("/user")
//    public User getUser(@RequestParam String key, @RequestParam String value) throws Exception {
//        return userService.find(key, value);
//    }
}
