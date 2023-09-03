package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.util.constant.ErrorMessages;
import antigravity.util.exception.ProductBadRequestException;
import org.springframework.stereotype.Service;

@Service
public class ProductPriceValidationService {

    public void isValidPriceRange(Product product) {
        double price = product.getPrice();
        if (price <= 10000 || price >= 10000000) {
            throw new ProductBadRequestException(ErrorMessages.INVALID_PRICE_RANGE);
        }
    }
}