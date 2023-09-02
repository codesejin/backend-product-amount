package antigravity.model.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInfoRequest {
    private int productId;
    private int[] couponIds;
}
