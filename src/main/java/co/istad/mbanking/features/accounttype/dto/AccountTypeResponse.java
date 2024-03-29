package co.istad.mbanking.features.accounttype.dto;

public record AccountTypeResponse(
        String name,
        String alias,
        String description
) {
}
