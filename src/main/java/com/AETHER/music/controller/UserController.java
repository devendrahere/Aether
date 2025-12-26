package com.AETHER.music.controller;

import com.AETHER.music.DTO.user.UserRegisterRequestDTO;
import com.AETHER.music.DTO.user.UserResponseDTO;
import com.AETHER.music.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public UserResponseDTO register(
            @Valid @RequestBody UserRegisterRequestDTO registerRequestDTO
    ){
        return userService.register(registerRequestDTO);
    }
}
