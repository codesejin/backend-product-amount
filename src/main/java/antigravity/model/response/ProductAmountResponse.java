package antigravity.model.response;

import antigravity.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ProductAmountResponse {
    private String name; //상품명
    private int originPrice; //상품 기존 가격
    private int discountPrice; //총 할인 금액
    private int finalPrice; //확정 상품 가격


    public static ProductAmountResponse entityToDto(Product product) {
        if (product != null) {
            return ProductAmountResponse.builder()
                    .name(product.getName())
                    .originPrice(product.getPrice())
                    .discountPrice(345)
                    .finalPrice(1234)
                    .build();
        } else {
            // product가 null일 경우 기본 값을 설정하거나 예외를 던질 수 있음
            // 기본 값 설정 예:
            return ProductAmountResponse.builder()
                    .name("Unknown Product")
                    .originPrice(0)
                    .discountPrice(0)
                    .finalPrice(0)
                    .build();
        }
    }
}
