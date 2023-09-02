package antigravity.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PromotionRepository promotionRepository;
    @Mock
    private PromotionProductsRepository promotionProductsRepository;
    @Mock
    private PriceCalculatorService priceCalculatorService;
    @Mock
    private ProductPriceValidationService productPriceValidationService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, promotionRepository, promotionProductsRepository,
                priceCalculatorService, productPriceValidationService);
    }

    private Product createProduct(int id, String name, int price) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }

    private Promotion createPromotion(int id, PromotionType promotionType, String name, int discountValue, DiscountType discountType, String useEndedAt) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return Promotion.builder()
                .id(id)
                .promotion_type(promotionType)
                .name(name)
                .discount_value(discountValue)
                .discount_type(discountType)
                .use_ended_at(format.parse(useEndedAt))
                .build();
    }

    @Test
    @DisplayName("1개 상품, 2개 프로모션 적용된 가격 추출")
    public void testGetProductAmount() throws ParseException {
        // given
        int productId = 1;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = new ProductInfoRequest(productId, couponIds);

        Product product = createProduct(1, "피팅노드상품", 215000);
        Promotion promotion1 = createPromotion(1, PromotionType.COUPON, "30000원 할인쿠폰", 30000, DiscountType.WON, "2022-11-01");
        Promotion promotion2 = createPromotion(2, PromotionType.CODE, "15% 할인코드", 15, DiscountType.PERCENT, "2022-11-01");

        // when
        when(promotionProductsRepository.findProductsWithPromotionIn(productId, Arrays.stream(request.getCouponIds()).boxed().collect(Collectors.toList())))
                .thenReturn(Arrays.asList(
                        PromotionProducts.builder().id(1).product(product).promotion(promotion1).build(),
                        PromotionProducts.builder().id(2).product(product).promotion(promotion2).build()
                ));

        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        assertThat(response.getName()).isEqualTo("피팅노드상품");
        assertThat(response.getOriginPrice()).isEqualTo(215000);
        assertThat(response.getDiscountPrice()).isEqualTo(62250);
        assertThat(response.getFinalPrice()).isEqualTo(150000);
    }

    @Test
    @DisplayName("Not Found Product Entity Exception 테스트")
    public void testEntityNotFoundException() {
        // given
        int productId = 1;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = new ProductInfoRequest(productId, couponIds);

        // when
        when(productRepository.findById(2)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productService.getProductAmount(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("해당하는 Product ID를 찾을 수 없습니다. : " + productId);
    }

    @Test
    @DisplayName("상품의 가격이  ₩ 10,000 미만일때 익셉션 테스트")
    public void testProductPriceMinimum() throws ParseException {
        // given
        int productId = 1;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = new ProductInfoRequest(productId, couponIds);

        Product product = createProduct(1, "1000원 상품", 1000);
        //when
        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        // then
        assertThatThrownBy(() -> productService.getProductAmount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 가격이 유효한 범위를 벗어납니다.");
    }

    @Test
    @DisplayName("상품의 가격이  ₩ 10,000,000 초과일때 익셉션 테스트")
    public void testProductPriceMaximum() throws ParseException {
        // given
        int productId = 1;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = new ProductInfoRequest(productId, couponIds);

        Product product = createProduct(1, "완전 비싼 상품", 20000000);
        //when
        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        // then
        assertThatThrownBy(() -> productService.getProductAmount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 가격이 유효한 범위를 벗어납니다.");
    }
}