package co.istad.mbanking.features.user.dto;

import co.istad.mbanking.domain.Role;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record UserCreateRequest(
        @NotNull(message = "Pin is required")
        @Max(value = 9999, message = "Pin must be 4 digits")
        @Positive
        Integer pin,

        @NotBlank(message = "Phone number is required")
        @Size(max = 20, message = "Phone number must be at less than 20 characters")
        String phoneNumber,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Confirm password is required")
        String confirmedPassword,

        @NotBlank(message = "Name is required")
        @Size(max = 40)
        String name,

        @NotBlank(message = "National id card is required")
        @Size(max = 20, message = "National id card must be at less than 20 characters")
        String nationalCardId,

        @NotBlank(message = "Gender is required")
        @Size(max = 6, message = "Gender must be at less 6 character")
        String gender,

        @NotBlank(message = "Student id card is required")
        @Size(max = 20, message = "Student id card must be at less than 20 characters")
        String studentIdCard,

        @NotNull(message = "Date of birth is required")
        LocalDate dob,

        @NotEmpty
        List<Role> roles
) {
}
