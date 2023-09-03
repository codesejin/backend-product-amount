package antigravity.service;

import antigravity.Utils;
import antigravity.domain.constants.DiscountType;
import antigravity.domain.constants.PromotionType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PriceCalculatorServiceTest {
    @InjectMocks
    private PriceCalculatorService priceCalculatorService;

    @Test
    @DisplayName("calculateDiscountPrice 메서드 - 할인 금액 계산 (원 단위)")
    public void calculateDiscountPrice_WON() throws ParseException {
        // given
        PriceCalculatorService priceCalculatorService = new PriceCalculatorService();
        Product product = Utils.createProduct(1, "피팅노드상품", 215000);
        Promotion promotion1 = Utils.createPromotion(1, PromotionType.COUPON, "30000원 할인쿠폰", 30000, DiscountType.WON, "2022-11-01");

        List<PromotionProducts> promotionProductsList = Collections.singletonList(
                PromotionProducts.builder().id(1).product(product).promotion(promotion1).build()
        );

        // when
        double discountPrice = priceCalculatorService.calculateDiscountPrice(product, promotionProductsList);

        // then
        Assertions.assertThat(discountPrice).isEqualTo(30000);
    }

    @Test
    @DisplayName("calculateDiscountPrice 메서드 - 할인 금액 계산 (퍼센트 단위)")
    public void calculateDiscountPrice_PERCENT() throws ParseException {
        // given
        PriceCalculatorService priceCalculatorService = new PriceCalculatorService();
        Product product = Utils.createProduct(1, "피팅노드상품", 215000);
        Promotion promotion1 = Utils.createPromotion(1, PromotionType.CODE, "15% 할인코드", 15, DiscountType.PERCENT, "2022-11-01");

        List<PromotionProducts> promotionProductsList = Collections.singletonList(
                PromotionProducts.builder().id(1).product(product).promotion(promotion1).build()
        );

        // when
        double discountPrice = priceCalculatorService.calculateDiscountPrice(product, promotionProductsList);

        // then
        Assertions.assertThat(discountPrice).isEqualTo(32250);
    }

    @Test
    @DisplayName("calculateDiscountPrice 메서드 - 총 할인가 추출")
    void calculateDiscountPrice() throws ParseException {
        // given
        Product product = Utils.createProduct(1, "피팅노드상품", 215000);

        Promotion promotion1 = Utils.createPromotion(1, PromotionType.COUPON, "30000원 할인쿠폰", 30000, DiscountType.WON, "2022-11-01");
        Promotion promotion2 = Utils.createPromotion(2, PromotionType.CODE, "15% 할인코드", 15, DiscountType.PERCENT, "2022-11-01");

        List<PromotionProducts> promotionProductsList = Arrays.asList(
                PromotionProducts.builder().id(1).product(product).promotion(promotion1).build(),
                PromotionProducts.builder().id(2).product(product).promotion(promotion2).build()
        );
        // when
        double discountPrice = priceCalculatorService.calculateDiscountPrice(product, promotionProductsList);

        // then
        Assertions.assertThat(discountPrice).isEqualTo(62250);
    }

    @Test
    @DisplayName("calculateFinalPrice 메서드 - 최종 가격 계산")
    public void calculateFinalPrice() {
        // given
        int originPrice = 215000;
        double discountPrice = 62250;

        // when
        int finalPrice = PriceCalculatorService.calculateFinalPrice(originPrice, discountPrice);

        // then
        Assertions.assertThat(finalPrice).isEqualTo(152000);
    }
}