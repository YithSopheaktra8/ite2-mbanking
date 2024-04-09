package co.istad.mbanking.features.transaction;


import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.Transaction;
import co.istad.mbanking.features.account.AccountRepository;
import co.istad.mbanking.features.transaction.dto.TransactionCreateRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import co.istad.mbanking.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;


    @Override
    public TransactionResponse transfer(TransactionCreateRequest transactionCreateRequest) {

        Account accountOwner = accountRepository.findByActNo(transactionCreateRequest.ownerActNo())
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account owner has not been found!"
                ));

        Account accountTransfer = accountRepository.findByActNo(transactionCreateRequest.transferReceiverActNo())
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account transfer has not been found!"
                ));

        if(accountOwner.getBalance().doubleValue() < transactionCreateRequest.amount().doubleValue()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient balance"
            );
        }

        if (transactionCreateRequest.amount().doubleValue() >= accountOwner.getTransferLimit().doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Transaction has been over the transfer limit");
        }

        accountOwner.setBalance(accountOwner.getBalance().subtract(transactionCreateRequest.amount()));

        accountTransfer.setBalance(accountTransfer.getBalance().add(transactionCreateRequest.amount()));

        Transaction transaction = transactionMapper.fromTransactionCreateRequest(transactionCreateRequest);
        transaction.setOwner(accountOwner);
        transaction.setTransferReceiver(accountTransfer);
        transaction.setTransactionType("TRANSFER");
        transaction.setStatus(true);
        transaction.setTransactionAt(LocalDateTime.now());
        transaction = transactionRepository.save(transaction);

        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    public Page<TransactionResponse> findAll(int page, int size, String transactionType, String sortDirection) {
        if(page < 0 ){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "page number must be greater than 0"
            );
        }

        if(size < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Size must be greater than or equal to 1!"
            );
        }

        Sort.Direction direction = Sort.Direction.DESC;

        if(sortDirection.equals("ASC")){
            direction = Sort.Direction.ASC;
        }

        Sort sortByDateTime = Sort.by(direction, "transactionAt");

        PageRequest pageRequest = PageRequest.of(page,size, sortByDateTime);

        Page<Transaction> transactions;

        if(transactionType.equals("TRANSFER")){
            transactions = transactionRepository.findAllByTransactionType(transactionType, pageRequest);
        } else if (transactionType.equals("PAYMENT")) {
            transactions = transactionRepository.findAllByTransactionType(transactionType, pageRequest);
        }else {
            transactions = transactionRepository.findAll(pageRequest);
        }

        return transactions.map(transactionMapper::toTransactionResponse);
    }


}
