package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;
import shop.helloshop.domain.entity.DeliveryStatus;

@Getter @Setter
public class OrderDto {

    private Long id;
    private String name;
    private int totalPrice;
    private int quantity;
    private DeliveryStatus deliveryStatus;

    public OrderDto() {
    }

    public OrderDto(Long id,String name, int totalPrice, int quantity, DeliveryStatus deliveryStatus) {
        this.id =id;
        this.name = name;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.deliveryStatus = deliveryStatus;
    }
}
