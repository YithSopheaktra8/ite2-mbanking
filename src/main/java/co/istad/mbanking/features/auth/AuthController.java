package co.istad.mbanking.features.auth;


import co.istad.mbanking.features.auth.dto.AuthResponse;
import co.istad.mbanking.features.auth.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {


    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return null;
    }

}