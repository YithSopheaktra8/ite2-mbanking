package co.istad.mbanking.features.user.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserEditRequest(
        String uuid,
        String name,
        String profileImage,
        String gender,
        LocalDate dob,
        String cityOrProvince,
        String khanOrDistrict,
        String sangkatOrCommune,
        String village,
        String street,
        String employeeType,
        String position,
        String companyName,
        String mainSourceOfIncome,
        BigDecimal monthlyIncomeRange
) {
}
