package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Transaction;
import co.istad.mbanking.features.transaction.dto.TransactionCreateRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public interface TransactionMapper {

    TransactionResponse toTransactionResponse(Transaction transaction);

    Transaction fromTransactionCreateRequest(TransactionCreateRequest transactionCreateRequest);


    List<TransactionResponse> ToListTransactionResponse(List<Transaction> transaction);
}
