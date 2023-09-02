package antigravity.domain.entity;

import antigravity.domain.constants.DiscountType;
import antigravity.domain.constants.PromotionType;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@DynamicUpdate
@DynamicInsert
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column
    private PromotionType promotion_type; //쿠폰 타입 (쿠폰, 코드)
    @Column
    private String name;
    @Enumerated(EnumType.STRING)
    @Column
    private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인
    @Column
    private int discount_value; // 할인 금액 or 할인 %
    @Column
    private Date use_started_at; // 쿠폰 사용가능 시작 기간
    @Column
    private Date use_ended_at; // 쿠폰 사용가능 종료 기간
    @OneToMany(mappedBy = "promotion")
    private List<PromotionProducts> promotionProducts;
}
