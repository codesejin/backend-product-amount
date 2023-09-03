package antigravity.repository;

import antigravity.util.constant.ErrorMessages;
import antigravity.util.exception.ProductBadRequestException;
import antigravity.util.exception.PromotionBadRequestException;
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
class PromotionRepositoryTest {
    @Autowired
    private PromotionRepository promotionRepository;

    @Test
    @DisplayName("existsByIdIn 메서드 - 유효한 프로모션 ID인 경")
    public void existsByIdIn() {
        // given
        int[] couponIds = {1, 2};
        List<Integer> list = Arrays.stream(couponIds).boxed().toList();
        // when
        boolean checkPromotionId = promotionRepository.existsByIdIn(list);
        // then
        Assertions.assertThat(checkPromotionId).isTrue();
    }

    @Test
    @DisplayName("existsByIds 메서드 - 유효하지 않은 프로모션 ID인 경우")
    public void existsByIds() {
        // given
        int[] couponIds = {3, 4};
        List<Integer> list = Arrays.stream(couponIds).boxed().toList();
        // when
        Assertions.assertThatThrownBy(() -> promotionRepository.existsByIds(list))
                .isInstanceOf(PromotionBadRequestException.class)
                .hasMessage(ErrorMessages.NOT_FOUND_PROMOTION);
    }
}