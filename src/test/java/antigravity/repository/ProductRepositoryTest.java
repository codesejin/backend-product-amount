package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.util.constant.ErrorMessages;
import antigravity.util.exception.ProductBadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Spring Data Jpa로 productId가 잘 검색 되는지 확인")
    void findById() {
        // given
        Product testProduct = Product.builder()
                .id(1)
                .name("피팅노드상품")
                .price(215000)
                .build();
        int productId = 1;
        // when
        Optional<Product> optionalProduct = productRepository.findById(productId);

        // then
        Assertions.assertThat(optionalProduct).isPresent(); // 해당 Id로 Optional이 비어있지 않은지 확인
        Product realProduct = optionalProduct.get();
        Assertions.assertThat(realProduct.getName()).isEqualTo(testProduct.getName());
        Assertions.assertThat(realProduct.getId()).isEqualTo(testProduct.getId());
        Assertions.assertThat(realProduct.getPrice()).isEqualTo(testProduct.getPrice());
    }

    @Test
    @DisplayName("getById 메서드 - 유효한 상품 ID인 경우")
    public void getByIdValidProduct() {
        // given
        int productId = 1;
        // when
        Product result = productRepository.getById(productId);
        // Assert
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("getById 메서드 - 유효하지 않은 상품 ID인 경우")
    public void getByIdInvalidProduct() {
        // given
        int productId = 2; // 존재하지 않는 상품 ID로 설정합니다.
        // when & then
        Assertions.assertThatThrownBy(() -> productRepository.getById(productId))
                .isInstanceOf(ProductBadRequestException.class)
                .hasMessage(ErrorMessages.NOT_FOUND_PRODUCT);
    }
}