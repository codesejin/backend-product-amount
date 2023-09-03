package antigravity.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PromotionBadRequestException extends RuntimeException{
    private final HttpStatus httpStatus;

    public PromotionBadRequestException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
    public PromotionBadRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
