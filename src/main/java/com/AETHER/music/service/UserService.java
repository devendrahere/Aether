package com.AETHER.music.service;

import com.AETHER.music.DTO.user.UserLoginRequestDTO;
import com.AETHER.music.DTO.user.UserLoginResponseDTO;
import com.AETHER.music.DTO.user.UserRegisterRequestDTO;
import com.AETHER.music.DTO.user.UserResponseDTO;
import com.AETHER.music.entity.User;

public interface UserService {

    UserResponseDTO register(UserRegisterRequestDTO dto);

    User getById(Long id);

   UserLoginResponseDTO login(UserLoginRequestDTO dto);
}
