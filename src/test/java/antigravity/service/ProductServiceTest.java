package antigravity.service;

import antigravity.Utils;
import antigravity.domain.constants.DiscountType;
import antigravity.domain.constants.PromotionType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionProductsRepository promotionProductsRepository;
    @Mock
    private PriceCalculatorService priceCalculatorService;
    @Mock
    private ProductPriceValidationService priceValidationService;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("getProductAmount 메서드 - 1개 상품, 2개 프로모션 적용된 가격 추출")
    public void getProductAmount() throws ParseException {
        // given
        int productId = 1;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = new ProductInfoRequest(productId, couponIds);

        Product product = Utils.createProduct(1, "피팅노드상품", 215000);

        given(productRepository.getById(1)).willReturn(product);
        Promotion promotion1 = Utils.createPromotion(1, PromotionType.COUPON, "30000원 할인쿠폰", 30000, DiscountType.WON, "2022-11-01", "2023-10-01");
        Promotion promotion2 = Utils.createPromotion(2, PromotionType.CODE, "15% 할인코드", 15, DiscountType.PERCENT, "2022-11-01","2023-10-01");

        // PromotionProducts 리스트를 변수에 저장
        List<PromotionProducts> promotionProductsList = Arrays.asList(
                PromotionProducts.builder().id(1).product(product).promotion(promotion1).build(),
                PromotionProducts.builder().id(2).product(product).promotion(promotion2).build()
        );

        given(promotionProductsRepository.findProductsWithPromotionIn(productId, request.intArrayToList(couponIds), new Date()))
                .willReturn(promotionProductsList);

        given(priceCalculatorService.calculateDiscountPrice(product, promotionProductsList))
                .willReturn(62250.0);
        // when
        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("피팅노드상품");
        assertThat(response.getOriginPrice()).isEqualTo(215000);
        assertThat(response.getDiscountPrice()).isEqualTo(62250);
        assertThat(response.getFinalPrice()).isEqualTo(152000);
    }
}