package shop.helloshop.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.helloshop.domain.entity.*;
import shop.helloshop.domain.service.MemberService;
import shop.helloshop.domain.service.OrderService;
import shop.helloshop.web.argumentresolver.Login;
import shop.helloshop.web.dto.MemberSessionDto;
import shop.helloshop.web.dto.OrderStatusDto;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final OrderService orderService;
    private final MemberService memberService;

    @GetMapping("/manager/order")
    public String managementForm(@Login MemberSessionDto memberSessionDto, Model model) {

        model.addAttribute("member",memberSessionDto);

        Member findMember = memberService.findOne(memberSessionDto.getId());

        if(!findMember.getMemberGrade().equals(MemberGrade.MANAGER)){
            return "redirect:/";
        }

        List<Order> findOrderList = orderService.findAllOrders();

        if (findOrderList.isEmpty()) {
            model.addAttribute("orderList",null);
            return "/manager/management";
        }

        List<OrderStatusDto> orderList = new ArrayList<>();

        for (Order order : findOrderList) {
            OrderStatusDto orderStatusDto = new OrderStatusDto(order.getId(),order.getMember().getEmail(),
                    order.getDeliveryStatus());

            orderList.add(orderStatusDto);
        }

        DeliveryStatus deliveryStatus = DeliveryStatus.READY;

        model.addAttribute("orderList",orderList);

        return "/manager/management";
    }

    @PostMapping("/manager/order/{id}")
    public String statusChange(@Login MemberSessionDto memberSessionDto, @PathVariable Long id) {

        Member findMember = memberService.findOne(memberSessionDto.getId());

        if(!findMember.getMemberGrade().equals(MemberGrade.MANAGER)){
            return "redirect:/";
        }

        Order findOrder = orderService.findOne(id);

        if(findOrder == null){
            return "redirect:/";
        }

        findOrder.setDeliveryStatus(DeliveryStatus.COMP);

        orderService.updateOrder(findOrder.getId(),findOrder);

        return "redirect:/manager/order";
    }
}
