package co.istad.mbanking.features.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionCreateRequest(

        @NotBlank(message = "Account no of  is required")
        String ownerActNo,

        @NotBlank(message = "Transfer receiver is required")
        String transferReceiverActNo,

        @NotNull(message = "Amount is required")
        @Positive
        BigDecimal amount,

        String remark

) {
}
