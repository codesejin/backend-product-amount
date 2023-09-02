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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromotionProductsRepository promotionProductsRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, promotionProductsRepository);
    }

    @Test
    @DisplayName("1개 상품, 2개 프로모션 적용된 가격 추출")
    public void testGetProductAmount() throws ParseException {
        // given
        int productId = 1;
        int[] couponIds = {1, 2};

        ProductInfoRequest request = new ProductInfoRequest(productId,couponIds);

        Product product = Product.builder()
                .id(1)
                .name("피팅노드상품")
                .price(215000)
                .build();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Promotion promotion1 = Promotion.builder()
                .id(1)
                .promotion_type(PromotionType.COUPON)
                .name("30000원 할인쿠폰")
                .discount_value(30000)
                .discount_type(DiscountType.WON)
                .use_ended_at(format.parse("2022-11-01"))
                .use_ended_at(format.parse("2023-03-01"))
                .build();

        Promotion promotion2 = Promotion.builder()
                .id(2)
                .promotion_type(PromotionType.CODE)
                .name("15% 할인코드")
                .discount_type(DiscountType.PERCENT)
                .discount_value(15)
                .use_ended_at(format.parse("2022-11-01"))
                .use_ended_at(format.parse("2023-03-01"))
                .build();

        PromotionProducts promotionProducts1 = PromotionProducts.builder()
                .id(1)
                .product(product)
                .promotion(promotion1)
                .build();

        PromotionProducts promotionProducts2 = PromotionProducts.builder()
                .id(2)
                .product(product)
                .promotion(promotion2)
                .build();

        List<PromotionProducts> promotionProductsList = new ArrayList<>();
        promotionProductsList.add(promotionProducts1);
        promotionProductsList.add(promotionProducts2);
        // when
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        when(promotionProductsRepository.findProductsWithPromotionIn(productId, Arrays.stream(request.getCouponIds()).boxed().collect(Collectors.toList())))
                .thenReturn(promotionProductsList);

        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        assertEquals(response.getName(), "피팅노드상품");
        assertEquals(response.getOriginPrice(), 215000);
        assertEquals(response.getDiscountPrice(), 62250);
        assertEquals(response.getFinalPrice(), 150000);

    }
}