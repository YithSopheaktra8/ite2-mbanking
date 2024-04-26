package co.istad.mbanking.features.auth;

import co.istad.mbanking.features.auth.dto.AuthResponse;
import co.istad.mbanking.features.auth.dto.ChangePasswordRequest;
import co.istad.mbanking.features.auth.dto.LoginRequest;
import co.istad.mbanking.features.auth.dto.RefreshTokenRequest;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse refresh(RefreshTokenRequest refreshTokenRequest);

    void changePassword(ChangePasswordRequest changePasswordRequest, Jwt jwt);

}
