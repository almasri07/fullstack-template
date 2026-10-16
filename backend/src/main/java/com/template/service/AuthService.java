package com.template.service;

import com.template.dto.AuthDto.*;
import com.template.entity.PasswordResetToken;
import com.template.entity.User;
import com.template.repository.PasswordResetTokenRepository;
import com.template.repository.UserRepository;
import com.template.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    // this method is to ensure that the email is unique and not
    // already in use by another user. If the email is unique, it creates
    // a new User object, encodes the password, saves the user to the database,
    // generates a JWT token for the user, and returns an AuthResponse containing
    // the user's information and the token.
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(User.Role.USER)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return toAuthResponse(user, token);
    }

    // The login method is responsible for authenticating a user
    // based on their email and password. It uses the AuthenticationManager
    // to perform the authentication. If the authentication is successful,
    // it retrieves the user from the database, generates a JWT token for
    // the user, and returns an AuthResponse containing the user's information and
    // the token.
    // If the user is not found, it throws an IllegalArgumentException.
    public AuthResponse login(LoginRequest request) {

        // check if the user exists in the database
        // if the user does not exist, throw an exception
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user);

        // return the AuthResponse containing the user's information and the token
        return toAuthResponse(user, token);
    }

    @Transactional
    public void requestPasswordReset(RequestPasswordResetRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken prt = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(prt);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken prt = tokenRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (prt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        tokenRepository.delete(prt);
    }

    @Transactional(readOnly = true)
    public AuthResponse getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toAuthResponse(user, null);
    }

    // Reads the already-authenticated principal straight from the security context (via
    // @AuthenticationPrincipal in the controller) instead of taking an arbitrary id, so a
    // caller can only ever fetch their own profile, never someone else's by guessing an id.
    @Transactional(readOnly = true)
    public AuthResponse getCurrentUser(User user) {
        return toAuthResponse(user, null);
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name());
    }
}
