package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

    private Long id;

    private String name;

    private int count;

    private int price;

    private int totalPrice;

    public OrderItemDto() {
    }

    public OrderItemDto(Long id, String name, int count, int price) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.price = price;
        this.totalPrice = this.price * this.count;
    }


}
