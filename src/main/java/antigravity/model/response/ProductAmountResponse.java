package antigravity.model.response;

import antigravity.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static antigravity.service.PriceCalculatorService.calculateFinalPrice;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAmountResponse {
    private String name; //상품명
    private int originPrice; //상품 기존 가격
    private int discountPrice; //총 할인 금액
    private int finalPrice; //확정 상품 가격


    public static ProductAmountResponse entityToResponse(Product product,double discountPrice) {
            return ProductAmountResponse.builder()
                    .name(product.getName())
                    .originPrice(product.getPrice())
                    .discountPrice((int)discountPrice)
                    .finalPrice(calculateFinalPrice(product.getPrice(), discountPrice))
                    .build();
    }
}
