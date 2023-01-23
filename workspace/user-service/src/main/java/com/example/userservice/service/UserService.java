package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;

public interface UserService  {
    //고객 등록
    UserDto createUser(UserDto userDto);

    //특정 사용자 조회
    UserDto getUserByUserId(String userId);
    //전체 정보 조회
    Iterable<UserEntity> getUserByAll();

    
}
