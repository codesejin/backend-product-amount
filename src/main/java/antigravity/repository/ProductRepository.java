package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.util.constant.ErrorMessages;
import antigravity.util.exception.ProductBadRequestException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    default Product getById(int productId){
        return findById(productId).orElseThrow(()-> new ProductBadRequestException(ErrorMessages.NOT_FOUND_PRODUCT));
    }
}
