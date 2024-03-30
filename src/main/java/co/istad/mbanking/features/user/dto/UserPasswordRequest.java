package co.istad.mbanking.features.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordRequest(
        @NotBlank
        @Size(max = 40)
        String name,

        @NotBlank
        String oldPassword,

        @NotBlank
        String NewPassword,

        @NotBlank
        String confirmedNewPassword
) {
}
