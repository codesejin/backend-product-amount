package antigravity.service;

import antigravity.domain.constants.DiscountType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionProductsRepository promotionProductsRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        List<PromotionProducts> productsAndPromotionIn = promotionProductsRepository.findProductsWithPromotionIn(
                request.getProductId(), Arrays.stream(request.getCouponIds()).boxed().collect(Collectors.toList()));

        Product product = null;
        double discountPrice = 0;
        // 상품 하나에 2개 프로모션 적용
        for (PromotionProducts promotionProduct : productsAndPromotionIn) {
            product = promotionProduct.getProduct();
            Promotion promotion = promotionProduct.getPromotion();
            if (promotion.getDiscount_type().equals(DiscountType.WON)) {
                discountPrice += promotion.getDiscount_value();
            }
            else {
                discountPrice += product.getPrice() * (promotion.getDiscount_value()/100.0);
            }
        }

        if (product == null) {
            // 상품이 설정되지 않았을 경우 기본 상품 정보를 가져옴
            product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("해당하는 Product ID를 찾을 수 없습니다. : " + request.getProductId()));
        }
        // 최소 상품가격 및 최대 상품가격 검증
        if (product.getPrice() < 10000 || product.getPrice() > 10000000) {
            throw new IllegalArgumentException("상품의 가격이 유효한 범위를 벗어납니다.");
        }

        return ProductAmountResponse.entityToResponse(product, discountPrice);
    }
}
