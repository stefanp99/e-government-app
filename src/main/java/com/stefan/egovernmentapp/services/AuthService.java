package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.models.Resident;
import com.stefan.egovernmentapp.models.Role;
import com.stefan.egovernmentapp.models.User;
import com.stefan.egovernmentapp.models.requests.LoginRequest;
import com.stefan.egovernmentapp.models.requests.RegisterRequest;
import com.stefan.egovernmentapp.repositories.UserRepository;
import com.stefan.egovernmentapp.utils.JwtUtil;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.stefan.egovernmentapp.utils.TwoFactorAuthUtil.generateSecretKey;
import static com.stefan.egovernmentapp.utils.TwoFactorAuthUtil.verifyCode;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ResidentService residentService;
    @Value("${authenticator.app.name}")
    private String issuer;

    public ResponseEntity<String> registerUser(RegisterRequest registerRequest, boolean isResident) {
        if (isResident)
            registerRequest.setRole(Role.RESIDENT);
        if (userRepository.findByEmailAddress(registerRequest.getEmailAddress()).isEmpty()) {
            User newUser = User.builder()
                    .emailAddress(registerRequest.getEmailAddress())
                    .isUsing2FA(registerRequest.getIsUsing2FA())
                    .password(registerRequest.getPassword())
                    .role(registerRequest.getRole())
                    .build();
            if (registerRequest.getIsUsing2FA()) {
                String otpAuthURL = add2FAFromUser(newUser);
                Resident resident = residentService.createResidentIfRoleIsResident(registerRequest.getEmailAddress());
                return resident == null ?
                        ResponseEntity.status(CREATED).body(format("OTP URL: %s", otpAuthURL)) :
                        ResponseEntity.status(CREATED).body(format("Resident created. OTP URL: %s", otpAuthURL));
            }
            userRepository.save(newUser);
            Resident resident = residentService.createResidentIfRoleIsResident(registerRequest.getEmailAddress());
            return resident == null ?
                    ResponseEntity.status(CREATED).body(format("User added successfully with ID: %d", newUser.getId())) :
                    ResponseEntity.status(CREATED).body(format("User added successfully with ID: %d and resident with ID %d ", newUser.getId(), resident.getId()));
        }
        return ResponseEntity.status(FORBIDDEN)
                .body(format("User already exists with email address: %s", registerRequest.getEmailAddress()));
    }

    public ResponseEntity<String> loginUser(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmailAddress(loginRequest.getEmailAddress());
        if (optionalUser.isPresent()) {
            User loggedUser = optionalUser.get();
            if (loggedUser.getIsUsing2FA())
                return verifyTotp(loggedUser, loginRequest.getTotpCode());
            return verifyPassword(loggedUser, loginRequest.getPassword());
        }
        return ResponseEntity.status(NOT_FOUND)
                .body(format("User not found with email address: %s", loginRequest.getEmailAddress()));
    }

    public ResponseEntity<String> add2FA(String token) {
        Optional<User> optionalUser = userRepository.findByEmailAddress(jwtUtil.extractEmailAddress(token.substring(7)));
        if (optionalUser.isPresent()) {
            User loggedUser = optionalUser.get();
            if (loggedUser.getIsUsing2FA())
                return ResponseEntity.status(UNAUTHORIZED)
                        .body("User already has 2FA");
            return ResponseEntity.ok(add2FAFromUser(loggedUser));
        }
        return ResponseEntity.status(NOT_FOUND)
                .body("User not found");
    }

    public ResponseEntity<String> remove2FA(String token) {
        Optional<User> optionalUser = userRepository.findByEmailAddress(jwtUtil.extractEmailAddress(token.substring(7)));
        if (optionalUser.isPresent()) {
            User loggedUser = optionalUser.get();
            if (loggedUser.getIsUsing2FA())
                return remove2FAFromUser(loggedUser);
            return ResponseEntity.status(UNAUTHORIZED).body("User does not have 2FA");
        }
        return ResponseEntity.status(NOT_FOUND)
                .body("User not found");
    }

    private String add2FAFromUser(User user) {
        GoogleAuthenticatorKey secretKey = generateSecretKey();
        String otpAuthURL = GoogleAuthenticatorQRGenerator
                .getOtpAuthURL(issuer, user.getEmailAddress(), secretKey);

        user.setSecretKey(secretKey.getKey());
        user.setIsUsing2FA(true);
        userRepository.save(user);
        return format("User added successfully using totp: %s", otpAuthURL);
    }

    private ResponseEntity<String> remove2FAFromUser(User user) {
        user.setSecretKey(null);
        user.setIsUsing2FA(false);
        userRepository.save(user);
        return ResponseEntity.ok("User removed 2FA");
    }

    private ResponseEntity<String> verifyPassword(User loggedUser, String requestPassword) {
        if (requestPassword.equals(loggedUser.getPassword())) {
            String token = jwtUtil.generateToken(loggedUser.getEmailAddress(), loggedUser.getRole());
            return ResponseEntity.status(OK).body(token);
        }
        return ResponseEntity.status(UNAUTHORIZED).body("Invalid credentials");
    }

    private ResponseEntity<String> verifyTotp(User loggedUser, String totpCode) {
        if (!totpCode.isEmpty() && verifyCode(loggedUser.getSecretKey(), totpCode)) {
            String token = jwtUtil.generateToken(loggedUser.getEmailAddress(), loggedUser.getRole());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(UNAUTHORIZED).body("Invalid TOTP code");
    }
}
