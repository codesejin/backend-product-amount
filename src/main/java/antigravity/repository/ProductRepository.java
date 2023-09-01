package antigravity.repository;

import antigravity.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product getProduct(int id) {
        //  sql 쿼리 문자열 정의
        String query = "SELECT * FROM `product` WHERE id = :id ";
        // Sql파라미터를 설정하기 위한 객체 생성
        MapSqlParameterSource params = new MapSqlParameterSource();
        // 객체에 sql 파라미터 id 추가
        params.addValue("id", id);
        //  SQL 쿼리를 실행하고 결과를 가져옵니다
        return namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> Product.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .price(rs.getInt("price"))
                        .build()

        );
    }
}
