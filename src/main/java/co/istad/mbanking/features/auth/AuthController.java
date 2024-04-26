package co.istad.mbanking.features.auth;


import co.istad.mbanking.features.auth.dto.AuthResponse;
import co.istad.mbanking.features.auth.dto.ChangePasswordRequest;
import co.istad.mbanking.features.auth.dto.LoginRequest;
import co.istad.mbanking.features.auth.dto.RefreshTokenRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refresh(refreshTokenRequest);
    }

    @PutMapping("/change-password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                               @AuthenticationPrincipal Jwt jwt){
        log.info("password info {}",changePasswordRequest.oldPassword());
        authService.changePassword(changePasswordRequest,jwt);
    }
}
