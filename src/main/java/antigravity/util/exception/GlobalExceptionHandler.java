package antigravity.util.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductBadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(ProductBadRequestException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PromotionBadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(PromotionBadRequestException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
