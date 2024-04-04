package co.istad.mbanking.exception;

import co.istad.mbanking.base.BaseError;
import co.istad.mbanking.base.BaseErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class MediaException {


    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseErrorResponse handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e){
        BaseError<String> baseError = BaseError.<String>builder()
                .code(HttpStatus.PAYLOAD_TOO_LARGE.getReasonPhrase())
                .description("Media upload size maximum is "+maxSize)
                .build();

        return new BaseErrorResponse(baseError);
    }
}
