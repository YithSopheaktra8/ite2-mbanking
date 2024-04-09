package co.istad.mbanking.features.account.dto;

import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.accounttype.dto.AccountTypeResponse;
import co.istad.mbanking.features.user.dto.UserDetailsResponse;
import co.istad.mbanking.features.user.dto.UserResponse;

import java.math.BigDecimal;

public record AccountResponse(
        String actNo,
        String alias,
        String actName,
        BigDecimal balance,
        BigDecimal transferLimit,
        AccountTypeResponse accountType,
        UserDetailsResponse user
) {
}
