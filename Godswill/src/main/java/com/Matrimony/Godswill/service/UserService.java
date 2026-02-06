package com.Matrimony.Godswill.service;

import com.Matrimony.Godswill.model.User;
import com.Matrimony.Godswill.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(User user) {
        user.onCreate();
        // In production, hash the password using BCrypt
        return userRepository.save(user);
    }

    public Optional<User> login(String emailOrPhone, String password) {
        Optional<User> user = userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            user.get().setLastLogin(LocalDateTime.now());
            userRepository.save(user.get());
            return user;
        }
        return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}