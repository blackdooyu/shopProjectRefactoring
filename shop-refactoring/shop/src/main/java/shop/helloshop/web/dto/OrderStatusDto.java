package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;
import shop.helloshop.domain.entity.DeliveryStatus;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderStatusDto {

    private Long id;
    private String email;
    private DeliveryStatus deliveryStatus;

    public OrderStatusDto(Long id, String email, DeliveryStatus deliveryStatus) {
        this.id = id;
        this.email = email;
        this.deliveryStatus = deliveryStatus;
    }

    public OrderStatusDto() {
    }
}
