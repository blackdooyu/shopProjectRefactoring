package shop.helloshop.domain.entity;

import lombok.Getter;
import lombok.Setter;
import shop.helloshop.web.exception.OrderException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();


    //생성메서드로만 생성하기 위해 생성자 막기
    protected Order() {

    }

    //생성 메서드
    public static Order createOrder(Member member ,List<OrderItem> orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setAddress(member.getAddress());
        order.setDeliveryStatus(DeliveryStatus.READY);

        for (OrderItem orderItem : orderItems) {
             order.addOrderItem(orderItem);
        }

        return order;
    }

    //주문취소 메서드
    public void cancel() {
        if (this.deliveryStatus.equals(DeliveryStatus.COMP)) {
            throw new OrderException("이미 배송된 상품입니다.");
        }
        this.deliveryStatus = DeliveryStatus.CANCEL;
        List<OrderItem> orderItems = this.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //연관관계 편의 메서드
    public void addOrderItem(OrderItem orderItem) {
        this.getOrderItems().add(orderItem);
        orderItem.setOrder(this);
    }

}
