package antigravity.domain.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Product {
    private int id;
    private String name;
    private int price;
}
