package com.mahesh;

import com.mahesh.dto.CreateUserRequest;
import com.mahesh.dto.GetUserResponse;
import com.mahesh.model.User;

public class Utils {

    public static User convertUserCreateRequest(CreateUserRequest request){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .age(request.getAge())
                .build();
    }

    public static GetUserResponse convertToUserResponse(User user){
        return GetUserResponse.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .age(user.getAge())
                .email(user.getEmail())
                .updatedOn(user.getUpdatedOn())
                .createdOn(user.getCreateOn())
                .build();
    }
}
