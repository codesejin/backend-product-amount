package antigravity.service;

import antigravity.Utils;
import antigravity.domain.entity.Product;
import antigravity.util.constant.ErrorMessages;
import antigravity.util.exception.ProductBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductPriceValidationServiceTest {
    @InjectMocks
    private ProductPriceValidationService priceValidationService;

    @Test
    @DisplayName("상품의 가격이  ₩ 10,000 미만일때 익셉션 테스트")
    public void testProductPriceMinimum() {
        // given
        Product product = Utils.createProduct(1, "1000원 상품", 1000);
        // when & then
        assertThatThrownBy(() -> priceValidationService.isValidPriceRange(product))
                .isInstanceOf(ProductBadRequestException.class)
                .hasMessage(ErrorMessages.INVALID_PRICE_RANGE);
    }

    @Test
    @DisplayName("상품의 가격이  ₩ 10,000,000 초과일때 익셉션 테스트")
    public void testProductPriceMaximum() {
        // given
        Product product = Utils.createProduct(1, "완전 비싼 상품", 20000000);
        // when & then
        assertThatThrownBy(() -> priceValidationService.isValidPriceRange(product))
                .isInstanceOf(ProductBadRequestException.class)
                .hasMessage(ErrorMessages.INVALID_PRICE_RANGE);
    }
}