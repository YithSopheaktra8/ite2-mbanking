package co.istad.mbanking.features.transaction;


import co.istad.mbanking.features.transaction.dto.TransactionCreateRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse transfer(@Valid @RequestBody TransactionCreateRequest transactionCreateRequest){
        return transactionService.transfer(transactionCreateRequest);
    }


    @GetMapping
    public Page<TransactionResponse> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "25") int page_size,
                                             @RequestParam(required = false, defaultValue = "") String transaction_type,
                                             @RequestParam(required = false, defaultValue = "DESC") String sort_date){

        return transactionService.findAll(page,page_size,transaction_type,sort_date);

    }


}
