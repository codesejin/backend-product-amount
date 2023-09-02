package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {
    @Query("SELECT pp FROM PromotionProducts pp" +
            " JOIN FETCH pp.product p" +
            " WHERE p.id = :productId AND pp.promotion.id IN :promotionIds")
    List<PromotionProducts> findProductsWithPromotionIn(@Param("productId") int productId,
                                                        @Param("promotionIds") List<Integer> promotionIds);
}
