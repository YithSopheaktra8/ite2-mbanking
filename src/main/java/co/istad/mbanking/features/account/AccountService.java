package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.AccountCreateRequest;
import co.istad.mbanking.features.account.dto.AccountRenameRequest;
import co.istad.mbanking.features.account.dto.AccountResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    void createNew(AccountCreateRequest accountCreateRequest);

    AccountResponse findAccountByActNo(String actNo);

    Page<AccountResponse> findListAccount(int page, int size);

    AccountResponse renameByActNo(String actNo, AccountRenameRequest accountRenameRequest);

    void hideAccount(String actNo);

    AccountResponse setLimitTransfer(String actNo,BigDecimal amount);

}
