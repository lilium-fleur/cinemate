package com.fleur.cinemate.user;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.auth.dto.RegisterDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(RegisterDto registerDto){
        userRepository.findByEmail(registerDto.email())
                .ifPresent(user -> {
                    throw new BadRequestException("User already exists");
                });
        User user = User.builder()
                .username(registerDto.username())
                .email(registerDto.email())
                .password(registerDto.password())
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public boolean matchPassword(String email, String password){
        User user = findUserByEmail(email);
        return passwordEncoder.matches(password, user.getPassword());
    }
}
