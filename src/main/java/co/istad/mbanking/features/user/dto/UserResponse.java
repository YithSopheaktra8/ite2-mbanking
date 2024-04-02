package co.istad.mbanking.features.user.dto;

import co.istad.mbanking.domain.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UserResponse(
        String uuid,
        String name,
        String profileImage,
        String gender,
        LocalDate dob
) {
}
