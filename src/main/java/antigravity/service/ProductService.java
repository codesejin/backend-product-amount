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

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionProductsRepository promotionProductsRepository;
    private final PriceCalculatorService priceCalculatorService;
    private final ProductPriceValidationService productPriceValidationService;


    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 Product ID를 찾을 수 없습니다. : " + request.getProductId()));

        List<Integer> couponIdsList = request.intArrayToList(request.getCouponIds());

        boolean checkCouponIds = promotionRepository.existsByIdIn(couponIdsList);
        if (!checkCouponIds) {
            throw new IllegalArgumentException("해당하는 Coupon ID를 찾을 수 없습니다. : " + Arrays.toString(request.getCouponIds()));
        }

        List<PromotionProducts> productsAndPromotionIn = promotionProductsRepository.findProductsWithPromotionIn(
                request.getProductId(), couponIdsList);

        // 1개 상품에 2개 프로모션 적용
        double discountPrice = priceCalculatorService.calculateDiscountPrice(product, productsAndPromotionIn);

        if (!productPriceValidationService.isValidPriceRange(product)) {
            throw new IllegalArgumentException("상품의 가격이 유효한 범위를 벗어납니다.");
        }

        return ProductAmountResponse.entityToResponse(product, discountPrice);
    }
}
