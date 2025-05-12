package com.time_tracker.be.service;

import com.time_tracker.be.exception.BadRequestException;
import com.time_tracker.be.exception.NotFoundException;
import com.time_tracker.be.model.ResponseModel;
import com.time_tracker.be.model.UserModel;
import com.time_tracker.be.repository.UserRepository;
import com.time_tracker.be.security.JwtTokenProvider;
import com.time_tracker.be.util.GoogleTokenUtil;
import com.time_tracker.be.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final String CLIENT_ID;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, @Value("${spring.security.oauth2.authorizationserver.client.google.client-id}") String clientId) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        CLIENT_ID = clientId;
    }

    public ResponseEntity<ResponseModel<String>> login(String email, String password) {
        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            throw new BadRequestException("Password salah atau email tidak terdaftar");
        }

        boolean passwordMatch = PasswordUtil.matches(password, user.getPassword());

        if (!passwordMatch) {
            throw new BadRequestException("Password salah atau email tidak terdaftar");
        }

        String token = jwtTokenProvider.createToken(user);
        ResponseModel<String> response = new ResponseModel<>(true, "Login berhasil", token);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ResponseModel<String>> loginWithGoogle(String idTokenString) {
        try {
            Map<String, Object> payload = GoogleTokenUtil.verifyGoogleToken(idTokenString);
            String email = (String) payload.get("email");
            boolean emailVerified = Boolean.parseBoolean((String) payload.get("email_verified"));
            String aud = (String) payload.get("aud");

            if (!emailVerified) {
                throw new BadRequestException("Email tidak terverifikasi");
            }

            if (!CLIENT_ID.equals(aud)) {
                throw new BadRequestException("Invalid audience");
            }

            UserModel user = userRepository.findByEmail(email);
            if (user == null) {
                throw new NotFoundException("Email tidak ditemukan");
            }

            String token = jwtTokenProvider.createToken(user);
            ResponseModel<String> response = new ResponseModel<>(true, "Login berhasil", token);
            return ResponseEntity.status(200).body(response);

        } catch (Exception e) {
            log.error("Login dengan Google gagal", e);
            throw new BadRequestException("Terjadi kesalahan saat login dengan Google");
        }
    }

    public ResponseEntity<ResponseModel<String>> register(String email, String password, String name) {
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(PasswordUtil.hashPassword(password));
        user.setCreatedAt(java.time.LocalDateTime.now());

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Mengecek jika constraint unique pada email tidak terpenuhi
            log.error(e.getMessage());
            throw new BadRequestException("Email sudah digunakan");
        }

        String token = jwtTokenProvider.createToken(user);
        ResponseModel<String> response = new ResponseModel<>(true, "Registrasi berhasil", token);
        return ResponseEntity.status(201).body(response);
    }
}
