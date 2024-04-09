package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.domain.UserAccount;
import co.istad.mbanking.features.account.dto.AccountCreateRequest;
import co.istad.mbanking.features.account.dto.AccountResponse;
import co.istad.mbanking.features.account.dto.AccountSnippetResponse;
import co.istad.mbanking.features.user.dto.UserDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        UserMapper.class,
        AccountTypeMapper.class
})

public interface AccountMapper {

    Account fromAccountCreateRequest(AccountCreateRequest accountCreateRequest);

    @Mapping(source = "userAccountList", target = "user",
            qualifiedByName = "mapUserDetailResponse")
    AccountResponse toAccountResponse(Account account);



    @Mapping(source = "userAccountList", target = "user",
            qualifiedByName = "mapUserDetailResponse")
    List<AccountResponse> toListAccountResponse(List<Account> accounts);

    AccountSnippetResponse toAccountSnippetResponse(Account account);

}
