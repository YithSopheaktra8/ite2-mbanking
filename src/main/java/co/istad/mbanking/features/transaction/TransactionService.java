package co.istad.mbanking.features.transaction;

import co.istad.mbanking.features.transaction.dto.TransactionCreateRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {

    TransactionResponse transfer(TransactionCreateRequest transactionCreateRequest);

    Page<TransactionResponse> findAll(int page, int size, String transactionType, String sortDirection);
}
