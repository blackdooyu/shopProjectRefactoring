package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;
import shop.helloshop.domain.entity.items.ItemSize;
import shop.helloshop.domain.entity.items.PhoneColor;

@Getter @Setter
public class ShopCartSession {

    private Long id;
    private int count;

}
