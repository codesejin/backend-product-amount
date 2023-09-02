package antigravity.repository;

import antigravity.domain.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    boolean existsByIdIn(List<Integer> promotionIds);

}
