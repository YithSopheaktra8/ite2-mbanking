package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.AccountType;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.domain.UserAccount;
import co.istad.mbanking.features.account.dto.AccountCreateRequest;
import co.istad.mbanking.features.account.dto.AccountRenameRequest;
import co.istad.mbanking.features.account.dto.AccountResponse;
import co.istad.mbanking.features.accounttype.AccountTypeRepository;
import co.istad.mbanking.features.accounttype.dto.AccountTypeResponse;
import co.istad.mbanking.features.user.UserRepository;
import co.istad.mbanking.features.user.dto.UserDetailsResponse;
import co.istad.mbanking.mapper.AccountMapper;
import co.istad.mbanking.mapper.AccountTypeMapper;
import co.istad.mbanking.mapper.UserMapper;
import co.istad.mbanking.utils.RandomDigit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountTypeRepository accountTypeRepository;
    private final UserAccountRepository userAccountRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;
    private final AccountTypeMapper accountTypeMapper;


    @Override
    public void createNew(AccountCreateRequest accountCreateRequest) {

        AccountType accountType = accountTypeRepository.findByAliasIgnoreCase(accountCreateRequest.accountTypeAlias())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Invalid account type")
                );

        // check user by UUID
        User user = userRepository.findByUuid(accountCreateRequest.userUuid())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User has not been found"));

        // map account dto to account entity
        Account account = accountMapper.fromAccountCreateRequest(accountCreateRequest);
        account.setAccountType(accountType);
        account.setActName(user.getName());
        account.setActNo("897898786");
        account.setTransferLimit(BigDecimal.valueOf(5000));
        account.setIsHidden(false);

        UserAccount userAccount = new UserAccount();
        userAccount.setAccount(account);
        userAccount.setUser(user);
        userAccount.setIsDeleted(false);
        userAccount.setIsBlocked(false);
        userAccount.setCreatedAt(LocalDateTime.now());

        userAccountRepository.save(userAccount);

    }

    @Override
    public AccountResponse findAccountByActNo(String actNo) {

        Account account = accountRepository.findByActNo(actNo)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account has not been found!"
                ));

        return accountMapper.toAccountResponse(account);
    }

    @Override
    public Page<AccountResponse> findListAccount(int page, int size) {

        if(page < 0 ){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "page number must be greater than 0"
            );
        }

        if(size < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Size must be greater than or equal to me!"
            );
        }

        List<Account> accountList = accountRepository.findAll();
        log.info("account {}",accountList);

        Sort sortByActName = Sort.by(Sort.Direction.ASC, "actName");
        PageRequest pageRequest = PageRequest.of(page,size, sortByActName);
        Page<Account> accounts = accountRepository.findAll(pageRequest);

        return accounts.map(accountMapper::toAccountResponse);
    }

    @Override
    public AccountResponse renameByActNo(String actNo, AccountRenameRequest accountRenameRequest) {

        Account account = accountRepository.findByActNo(actNo)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "account has not been found!"
                ));

        if(account.getAlias().equals(accountRenameRequest.newName()) && account.getAlias() != null){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Account name must not be the old one!"
            );
        }

        account.setAlias(accountRenameRequest.newName());
        account = accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }

    @Transactional
    @Override
    public void hideAccount(String actNo) {

        if(!accountRepository.existsByActNo(actNo)){
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Account has not been found!"
            );
        }

        try{
            accountRepository.hideAccountByActNo(actNo);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something when wrong!!"
            );
        }

    }

    @Override
    public AccountResponse setLimitTransfer(String actNo,BigDecimal amount) {

        Account account = accountRepository.findByActNo(actNo)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "account has not been found!"
                ));

        account.setTransferLimit(amount);

        account = accountRepository.save(account);

        return accountMapper.toAccountResponse(account);

    }


}
