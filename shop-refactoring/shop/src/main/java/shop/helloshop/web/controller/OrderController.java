package shop.helloshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.helloshop.domain.entity.Order;
import shop.helloshop.domain.entity.OrderItem;
import shop.helloshop.domain.entity.items.Item;
import shop.helloshop.domain.service.ItemService;
import shop.helloshop.domain.service.OrderService;
import shop.helloshop.web.argumentresolver.Login;
import shop.helloshop.web.dto.*;
import shop.helloshop.web.exception.ItemException;
import shop.helloshop.web.exception.OrderException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;


    @PostMapping("/shopCart/delete")
    public String shopCartDelete(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionKey.CART_SESSION);
        return "redirect:/order";
    }


    @GetMapping("/order")
    public String orderForm(Model model, HttpServletRequest request, @Login MemberSessionDto memberSessionDto,
                            @RequestParam(name = "error",defaultValue = "") String error) {

        model.addAttribute("member",memberSessionDto);

        if (!error.isEmpty()){
            model.addAttribute("error",error);
        }

        HttpSession session = request.getSession(false);
        Object sessionList = session.getAttribute(SessionKey.CART_SESSION);

        int totalPrice = 0;

        if(sessionList == null){
            model.addAttribute("orderItem",null);
            return "/order/shopCart";
        }

            List<ShopCartSession> cartList = (List<ShopCartSession>)sessionList;
            List<OrderItemDto> orderItemDtoList = new ArrayList<>();

            for (ShopCartSession shopCartSession : cartList) {
                Item findItem = itemService.findOne(shopCartSession.getId());
                OrderItemDto orderItemDto = new OrderItemDto(findItem.getId(),findItem.getName(), shopCartSession.getCount()
                        ,findItem.getPrice());
                orderItemDtoList.add(orderItemDto);
                totalPrice += orderItemDto.getTotalPrice();
            }

            model.addAttribute("orderItem",orderItemDtoList);
            model.addAttribute("totalPrice",totalPrice);



        return "/order/shopCart";

    }

    @PostMapping("/order")
    public String orderAdd(@Login MemberSessionDto memberSessionDto,HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        Object list = session.getAttribute(SessionKey.CART_SESSION);

        if (list == null){
            return "redirect:/";
        }

        List<ShopCartSession> shopCartList = (List<ShopCartSession>) list;

        try {
            orderService.order(memberSessionDto.getId(), shopCartList);
        } catch (ItemException e) {
            redirectAttributes.addAttribute("error",e.getMessage());
            return "redirect:/order";
        }

        session.removeAttribute(SessionKey.CART_SESSION);


        return "redirect:/";
    }

    @GetMapping("/order/list")
    public String orderListForm(@Login MemberSessionDto memberSessionDto,Model model) {

        List<Order> orderList = orderService.findMemberOrders(memberSessionDto.getId());

        model.addAttribute("member",memberSessionDto);

        if (orderList.isEmpty()){
            model.addAttribute("orderList",null);
            return "order/orderList";
        }

        List<OrderDto> orderDtoList = orderListView(orderList);

        model.addAttribute("orderList",orderDtoList);
        return "order/orderList";
    }

    @GetMapping("/order/cancel/{id}")
    public String orderCancel(@PathVariable Long id, @Login MemberSessionDto memberSessionDto) throws IOException {

        if (id == null){
            return "redirect:/order/list";
        }

        Order findOrder = orderService.findOne(id);

        if(findOrder == null){
            return "redirect:/order/list";
        }

        if (findOrder.getMember().getId() == memberSessionDto.getId()) {
            try {
                orderService.orderCancel(id);
            } catch (OrderException e) {
                log.info("====={}====",e.getMessage());
                return "redirect:/order/list";
            }
        }

        return "redirect:/order/list";
    }


    //주문 목록 생성
    private List<OrderDto> orderListView(List<Order> orderList) {

            List<OrderDto> orderDtoList = new ArrayList<>();
            for (Order order : orderList) {
                List<OrderItem> orderItemList = order.getOrderItems();
                for (OrderItem orderItem : orderItemList) {
                    OrderDto orderDto = new OrderDto(order.getId(),orderItem.getName(),orderItem.getOrderPrice(),orderItem.getCount(),order.getDeliveryStatus());
                    orderDtoList.add(orderDto);
                }
            }
        return orderDtoList;
    }

}
