package com.store.service;

import com.store.models.UserDto;
import com.store.entity.UserEntity;

public interface IUserService {
    UserEntity registerNewUserAccount(UserDto userDto);
}
