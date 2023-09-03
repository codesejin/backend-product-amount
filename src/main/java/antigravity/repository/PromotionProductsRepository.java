package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {
    @Query("SELECT pp FROM PromotionProducts pp" +
            " JOIN FETCH pp.product p" +
            " JOIN pp.promotion pm" +
            " WHERE p.id = :productId" +
            " AND pm.id IN :promotionIds" +
            " AND pm.use_started_at <= :today" +
            " AND pm.use_ended_at >= :today")
    List<PromotionProducts> findProductsWithPromotionIn(@Param("productId") int productId,
                                                        @Param("promotionIds") List<Integer> promotionIds,
                                                        @Param("today") Date today);
}
