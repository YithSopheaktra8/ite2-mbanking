package co.istad.mbanking.features.user.dto;

import co.istad.mbanking.domain.Role;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record UserCreateRequest(
        @NotNull
        @Max(9999)
        @Positive
        Integer pin,

        @NotBlank
        @Size(max = 20)
        String phoneNumber,

        @NotBlank
        String password,

        @NotBlank
        String confirmedPassword,

        @NotBlank
        @Size(max = 40)
        String name,

        @NotBlank
        @Size(max = 20)
        String nationalCardId,

        @NotBlank
        @Size(max = 6)
        String gender,

        @NotBlank
        @Size(max = 20)
        String studentIdCard,

        @NotNull
        LocalDate dob,

        @NotEmpty
        List<Role> roles
) {
}
