package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.user.UserLoginRequestDTO;
import com.AETHER.music.DTO.user.UserLoginResponseDTO;
import com.AETHER.music.DTO.user.UserRegisterRequestDTO;
import com.AETHER.music.DTO.user.UserResponseDTO;
import com.AETHER.music.auth.CustomUserDetails;
import com.AETHER.music.entity.User;
import com.AETHER.music.exception.ConflictException;
import com.AETHER.music.exception.ResourceNotFoundException;
import com.AETHER.music.exception.UnauthorizedException;
import com.AETHER.music.jwt.JwtService;
import com.AETHER.music.repository.UserRepository;
import com.AETHER.music.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService=jwtService;
        this.authenticationManager=authenticationManager;
    }

    @Override
    public UserResponseDTO register(UserRegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.email)) {
            throw new ConflictException("Email already in use");
        }

        if (userRepository.existsByUsername(dto.username)) {
            throw new ConflictException("Username already taken");
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserLoginResponseDTO login(UserLoginRequestDTO dto) {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email,
                        dto.password
                )
        );

        CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();

        String token =jwtService.generateToken(userDetails);

        User user= userDetails.getUser();

        UserLoginResponseDTO responseDTO=new UserLoginResponseDTO();
        responseDTO.token=token;
        responseDTO.userId=user.getId();
        responseDTO.username=user.getUsername();

        return responseDTO;
    }
}
