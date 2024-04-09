package co.istad.mbanking.exception;

import co.istad.mbanking.base.BaseError;
import co.istad.mbanking.base.BaseErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ServiceException {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleServiceError(ResponseStatusException ex){

        BaseError<String> baseError = new BaseError<>();
        baseError.setCode(ex.getStatusCode().toString());
        baseError.setDescription(ex.getReason());

        BaseErrorResponse baseErrorResponse = BaseErrorResponse.builder()
                .baseError(baseError)
                .build();

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(baseErrorResponse);
    }

}
