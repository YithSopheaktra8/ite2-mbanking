package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.features.accounttype.dto.AccountTypeResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {

    List<AccountTypeResponse> toListAccountTypeResponse(List<AccountType> accountTypes);

    AccountTypeResponse toAccountTypeResponse(AccountType accountType);

}
