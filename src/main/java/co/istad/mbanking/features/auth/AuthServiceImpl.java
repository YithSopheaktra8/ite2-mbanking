package co.istad.mbanking.features.auth;


import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.auth.dto.AuthResponse;
import co.istad.mbanking.features.auth.dto.ChangePasswordRequest;
import co.istad.mbanking.features.auth.dto.LoginRequest;
import co.istad.mbanking.features.auth.dto.RefreshTokenRequest;
import co.istad.mbanking.features.token.AuthTokenService;
import co.istad.mbanking.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final AuthTokenService tokenService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.phoneNumber(),
                loginRequest.password()
        );

        authentication = daoAuthenticationProvider.authenticate(authentication);

        return tokenService.createToken(authentication);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest) {

        Authentication authentication = new BearerTokenAuthenticationToken(
                refreshTokenRequest.refreshToken()
        );

        authentication = jwtAuthenticationProvider.authenticate(authentication);


        return tokenService.createToken(authentication);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest, Jwt jwt) {

        log.info("oldPassword : {}",changePasswordRequest.oldPassword());
        log.info("password : {}",changePasswordRequest.newPassword());
        log.info("confirmPassword : {}",changePasswordRequest.confirmPassword());

        User user = userRepository.findByPhoneNumber(jwt.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        if (!changePasswordRequest.newPassword().equals(changePasswordRequest.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "password does not match!"
            );
        }

        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Old password does not match!"
            );
        }

        if (passwordEncoder.matches(changePasswordRequest.newPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "New password is not allowed!"
            );
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
        userRepository.save(user);

    }
}
