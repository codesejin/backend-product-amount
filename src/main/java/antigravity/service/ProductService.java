package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionProductsRepository promotionProductsRepository;
    private final PriceCalculatorService priceCalculatorService;
    private final ProductPriceValidationService productPriceValidationService;

    @Transactional(readOnly = true)
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        Product product = productRepository.getById(request.getProductId());
        productPriceValidationService.isValidPriceRange(product);

        List<Integer> couponIdsList = request.intArrayToList(request.getCouponIds());
        promotionRepository.existsByIds(couponIdsList);

        List<PromotionProducts> productsAndPromotionIn = promotionProductsRepository.findProductsWithPromotionIn(
                request.getProductId(), couponIdsList, new Date());

        double discountPrice = priceCalculatorService.calculateDiscountPrice(product, productsAndPromotionIn);

        return ProductAmountResponse.entityToResponse(product, discountPrice);
    }
}
