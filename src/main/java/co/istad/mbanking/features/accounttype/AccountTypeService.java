package co.istad.mbanking.features.accounttype;

import co.istad.mbanking.features.accounttype.dto.AccountTypeResponse;

import java.util.List;

public interface AccountTypeService {

    List<AccountTypeResponse> findAll();

    AccountTypeResponse findByAlias(String alias);
}
