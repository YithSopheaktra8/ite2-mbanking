package co.istad.mbanking.features.token;

import co.istad.mbanking.features.auth.dto.AuthResponse;
import org.springframework.security.core.Authentication;

public interface AuthTokenService {

    AuthResponse createToken(Authentication authentication);

    String createAccessToken(Authentication authentication);

    String createRefreshToken(Authentication authentication);

}
