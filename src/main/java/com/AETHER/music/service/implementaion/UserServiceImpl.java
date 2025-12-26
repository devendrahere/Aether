package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.user.UserRegisterRequestDTO;
import com.AETHER.music.DTO.user.UserResponseDTO;
import com.AETHER.music.entity.User;
import com.AETHER.music.repository.UserRepository;
import com.AETHER.music.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO register(UserRegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.email)) {
            throw new IllegalStateException("Email already in use");
        }

        if (userRepository.existsByUsername(dto.username)) {
            throw new IllegalStateException("Username already taken");
        }

        User user = new User();
        user.setEmail(dto.email);
        user.setUsername(dto.username);
        user.setPasswordHash(passwordEncoder.encode(dto.password));

        userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();
        response.id = user.getId();
        response.username = user.getUsername();
        response.profileImageUrl = user.getProfileImageUrl();

        return response;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
