package co.istad.mbanking.features.user.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserCreateRequest(
        @NotNull
        @Min(4)
        @Max(4)
        @Positive
        Integer pin,

        @NotBlank
        @Size(max = 20)
        String phoneNumber,

        @NotBlank
        String password,

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
        String studentId,

        @NotNull
        LocalDate dob
) {
}
