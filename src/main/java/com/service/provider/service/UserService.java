package com.service.provider.service;

import java.util.List;
import com.service.provider.dto.UserDto;
import com.service.provider.model.User;

public interface UserService {
    UserDto getUserById(long id);

    List<UserDto> getAllUsers();

    User loadUserByUsername(String username);

    public String createUser(UserDto user);

    UserDto getUserByUserName(String userName);
}
