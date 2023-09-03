package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PromotionProductsRepositoryTest {

    @Autowired
    private PromotionProductsRepository promotionProductsRepository;

    @Test
    @DisplayName("findProductsWithPromotionIn 메서드 - 쿼리에 해당하는 데이터가 나올 경우")
    void returnDataWhenQueryMatches() {
        // given
        int productId = 1;
        int[] couponIds = {1,2};
        List<Integer> list = Arrays.stream(couponIds).boxed().toList();
        // when
        List<PromotionProducts> productsWithPromotionIn = promotionProductsRepository.findProductsWithPromotionIn(productId, list);
        // then
        Assertions.assertThat(productsWithPromotionIn).isNotEmpty(); // 빈 배열이 아닌 경우
        // 예상한 데이터가 포함되어 있는지 확인
        Assertions.assertThat(productsWithPromotionIn).anySatisfy(promotionProducts -> {
            Assertions.assertThat(promotionProducts.getProduct().getId()).isEqualTo(productId);
            Assertions.assertThat(promotionProducts.getProduct().getName()).isEqualTo("피팅노드상품");
            Assertions.assertThat(promotionProducts.getPromotion().getId()).isIn(list);
            Assertions.assertThat(promotionProducts.getPromotion().getName()).isEqualTo("30000원 할인쿠폰");
        });
    }

    @Test
    @DisplayName("findProductsWithPromotionIn 메서드 - 쿼리에 해당하는 데이터가 없을 경우")
    void returnDataWhenQueryDiesNotMatch() {
        // given
        int productId = 1;
        int[] couponIds = {3,4};
        List<Integer> list = Arrays.stream(couponIds).boxed().toList();
        // when
        List<PromotionProducts> productsWithPromotionIn = promotionProductsRepository.findProductsWithPromotionIn(productId, list);
        // then
        Assertions.assertThat(productsWithPromotionIn).isEmpty(); // 빈 배열인 경우
    }
}