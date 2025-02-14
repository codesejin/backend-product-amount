package antigravity.repository;

import antigravity.domain.entity.Promotion;
import antigravity.util.constant.ErrorMessages;
import antigravity.util.exception.PromotionBadRequestException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    boolean existsByIdIn(List<Integer> promotionIds);

    default void existsByIds(List<Integer> promotionIds) {
        if (!existsByIdIn(promotionIds)) {
            throw new PromotionBadRequestException(ErrorMessages.NOT_FOUND_PROMOTION);
        }
    }
}
