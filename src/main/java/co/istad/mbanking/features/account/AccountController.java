package co.istad.mbanking.features.account;

import co.istad.mbanking.features.account.dto.AccountCreateRequest;
import co.istad.mbanking.features.account.dto.AccountRenameRequest;
import co.istad.mbanking.features.account.dto.AccountResponse;
import co.istad.mbanking.utils.BigDecimalUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createNew(@Valid @RequestBody AccountCreateRequest accountCreateRequest){
        accountService.createNew(accountCreateRequest);
    }

    @GetMapping("/{actNo}")
    AccountResponse findByActNumber(@PathVariable String actNo){

        return accountService.findAccountByActNo(actNo);
    }

    @GetMapping
    Page<AccountResponse> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "25") int limit
    ){
        return accountService.findListAccount(page,limit);
    }

    @PutMapping("/{actNo}/rename")
    AccountResponse renameByActNo(@PathVariable String actNo,
                                  @Valid @RequestBody AccountRenameRequest accountRenameRequest){
        return accountService.renameByActNo(actNo,accountRenameRequest);
    }

    @PutMapping("/{actNo}/hide")
    void hideAccount(@PathVariable String actNo){
        accountService.hideAccount(actNo);
    }

    @PutMapping("/{actNo}/transfer-limit")
    AccountResponse setTransferLimit(@PathVariable String actNo,
                                     @RequestBody BigDecimalUtil transferLimitAmount){
        BigDecimal amount = transferLimitAmount.getTransferLimitAmount();
        return accountService.setLimitTransfer(actNo,amount);
    }

}
