package co.istad.mbanking.features.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountRenameRequest(
        @NotBlank(message = "Account new name is required")
        @Size(message = "Account new name must be at less 100 characters")
        String newName
) {
}
