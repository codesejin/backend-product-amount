package antigravity.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductBadRequestException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ProductBadRequestException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
    public ProductBadRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
