package co.istad.mbanking.features.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserProfileImageRequest(
        @NotBlank
        String mediaName
) {
}
