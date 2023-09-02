package antigravity.service;

import antigravity.domain.constants.DiscountType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceCalculatorService {
    public double calculateDiscountPrice(Product product, List<PromotionProducts> promotionProducts) {
        double discountPrice = 0;
        for (PromotionProducts promotionProduct : promotionProducts) {
            Promotion promotion = promotionProduct.getPromotion();
            if (promotion.getDiscount_type().equals(DiscountType.WON)) {
                discountPrice += promotion.getDiscount_value();
            } else {
                discountPrice += product.getPrice() * (promotion.getDiscount_value() / 100.0);
            }
        }
        return discountPrice;
    }

    public static int calculateFinalPrice(int originPrice, double discountPrice) {
        int priceBeforeTrimming = (int) (originPrice - discountPrice);
        // 천단위 절삭
        return (priceBeforeTrimming / 10000) * 10000;
    }
}
