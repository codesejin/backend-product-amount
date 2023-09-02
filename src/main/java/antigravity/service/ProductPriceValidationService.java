package antigravity.service;

import antigravity.domain.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductPriceValidationService {

    public boolean isValidPriceRange(Product product) {
        double price = product.getPrice();
        return price >= 10000 && price <= 10000000;
    }
}