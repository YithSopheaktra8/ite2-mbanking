package co.istad.mbanking.exception;

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
        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of("error",ex.getReason()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> handleValidationError(MethodArgumentNotValidException ex){
        List<Map<String, Object>> errorList = new ArrayList<>();
        ex.getFieldErrors().stream()
                .forEach(fieldError -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("field",fieldError.getField());
                    error.put("message",fieldError.getDefaultMessage());
                    errorList.add(error);
                });
        return Map.of("error",errorList);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.REQUEST_ENTITY_TOO_LARGE)
    public Map<String, Object> handleFileUpload(MaxUploadSizeExceededException ex){
        Map<String,Object> error = new HashMap<>();
        error.put("max file upload size", ex.getMaxUploadSize());
        error.put("message : ", ex.getLocalizedMessage());
        List<Map<String,Object>> errorList = new ArrayList<>();
        errorList.add(error);
        return Map.of("error",errorList);
    }

}
