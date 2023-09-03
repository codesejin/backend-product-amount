package antigravity;

import antigravity.domain.constants.DiscountType;
import antigravity.domain.constants.PromotionType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static Product createProduct(int id, String name, int price) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }

    public static Promotion createPromotion(int id, PromotionType promotionType, String name, int discountValue, DiscountType discountType, String useStartedAt, String useEndedAt) throws ParseException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date usestartedDate = format.parse(useStartedAt);
        Date useEndedDate = format.parse(useEndedAt);
        return Promotion.builder()
                .id(id)
                .promotion_type(promotionType)
                .name(name)
                .discount_value(discountValue)
                .discount_type(discountType)
                .use_started_at(usestartedDate)
                .use_ended_at(useEndedDate)
                .build();
    }
}
