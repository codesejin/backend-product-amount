package antigravity.model.request;

import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInfoRequest {
    private int productId;
    private int[] couponIds;

    public List<Integer> intArrayToList(int[] array) {
        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }
}
