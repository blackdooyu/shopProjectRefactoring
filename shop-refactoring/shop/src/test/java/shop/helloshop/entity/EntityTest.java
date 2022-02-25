package shop.helloshop.entity;

import org.junit.jupiter.api.Test;
import shop.helloshop.domain.entity.*;
import shop.helloshop.domain.entity.items.Item;
import shop.helloshop.domain.entity.items.Phone;
import shop.helloshop.web.dto.MemberDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    void Member_생성() {
        //given
        Member member = Member.createMember("test@naver.com", "zxc123", "생성", new Address("서울", "강남", "123"));

        //when

        //then
        assertEquals(member.getName(),"생성");
        assertEquals(member.getEmail(),"test@naver.com");
        assertEquals(member.getPassword(),"zxc123");
        assertEquals(member.getAddress(), new Address("서울", "강남", "123"));
    }

    @Test
    void Member_업데이트() {
        //given
        Member member = Member.createMember("test@naver.com", "zxc123", "생성", new Address("서울", "강남", "123"));
        MemberDto updateForm = MemberDto.createUpdateForm("update@naver.com", "업데이트", new Address("업데이트", "업데이트", "업데이트"));
        updateForm.setPassword("update");

        //when
        member.updateMember(updateForm);

        //then
        assertEquals(member.getName(),"업데이트");
        assertEquals(member.getEmail(),"update@naver.com");
        assertEquals(member.getPassword(),"update");
        assertEquals(member.getAddress(), new Address("업데이트", "업데이트", "업데이트"));
    }

    @Test
    void Item_판매() {
        //given
        Item item = getItem();

        //when
        item.sale(3);


        //then
       assertEquals(item.getQuantity(),12);
    }

    @Test
    void Item_취소() {
        //given
        Item item = getItem();
        item.sale(3);

        //when
        item.cancel(3);

        //then
        assertEquals(item.getQuantity(),15);
    }

    @Test
    void Item_업데이트() {
        //given
        Item item = getItem();
        item.sale(3);

        //when
        item.update("update",100,10);

        //then
        assertEquals(item.getName(),"update");
        assertEquals(item.getQuantity(),10);
        assertEquals(item.getPrice(),100);
    }

    @Test
    void OrderItem_생성() {
        //given
        OrderItem orderItem = OrderItem.createOrderItem(getItem(), 5);

        //when


        //then
        assertEquals(orderItem.getOrderPrice(),orderItem.getItem().getTotalPrice(5));
        assertEquals(orderItem.getCount(),5);

    }

    @Test
    void OrderItem_취소() {
        //given
        OrderItem orderItem = OrderItem.createOrderItem(getItem(), 5);

        //when
        orderItem.cancel();


        //then
        assertEquals(orderItem.getItem().getQuantity(),15);


    }

    @Test
    void Order_생성() {
        //given
        OrderItem orderItem = OrderItem.createOrderItem(getItem(), 5);
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);
        Member member = Member.createMember("test@naver.com", "zxc123", "생성", new Address("서울", "강남", "123"));
        //when
        Order order = Order.createOrder(member, orderItemList);


        //then
        assertEquals(order.getAddress(), new Address("서울", "강남", "123"));
        assertEquals(order.getDeliveryStatus(), DeliveryStatus.READY);
        assertEquals(order.getOrderItems().size(),1);

    }

    @Test
    void Order_취소() {
        //given
        OrderItem orderItem = OrderItem.createOrderItem(getItem(), 5);
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);
        Member member = Member.createMember("test@naver.com", "zxc123", "생성", new Address("서울", "강남", "123"));
        Order order = Order.createOrder(member, orderItemList);

        //when
        order.cancel();


        //then
        assertEquals(order.getDeliveryStatus(), DeliveryStatus.CANCEL);

    }

    private Item getItem() {
        Item item = new Phone();
        item.setPrice(500);
        item.setName("상품1");
        item.setQuantity(15);
        item.setSalesQuantity(0);
        return item;
    }


}
