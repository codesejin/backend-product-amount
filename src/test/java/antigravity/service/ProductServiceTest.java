package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("Product 테이블 연결 테스트")
    public void testGetProductAmount() {
        // 테스트에 사용할 가상 데이터 생성
        int productId = 1;
        int[] couponIds = {1, 2};

        ProductInfoRequest request = new ProductInfoRequest(1,couponIds);
        Product sampleProduct = new Product(productId, "피팅노드상품", 215000);

        // getProduct 메서드가 호출될 때 반환할 가상 데이터 설정
        when(productRepository.getProduct(productId)).thenReturn(sampleProduct);

        // getProductAmount 메서드 호출
        ProductAmountResponse response = productService.getProductAmount(request);

        // 예상 결과와 실제 결과 비교
        assertEquals(sampleProduct.getName(), response.getName());
        assertEquals(sampleProduct.getPrice(), response.getOriginPrice());
    }
}