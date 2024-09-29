package com.order.order.controller;

import com.base.base.dto.OrderEventDto;
import com.order.order.common.OrderResponse;
import com.order.order.dto.OrderDTO;
import com.order.order.kafka.OrderProducer;
import com.order.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProducer orderProducer;

    @GetMapping("/getorders")
    public List<OrderDTO> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/order/{orderId}")
    public OrderDTO getOrderById(@PathVariable Integer orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping("/addorder")
    public OrderResponse saveOrder(@RequestBody OrderDTO orderDTO) {

        OrderResponse orderResponse = orderService.saveOrder(orderDTO);

        OrderEventDto  orderEventDto = new OrderEventDto();
        orderEventDto.setMessage("Order is Committed Order id " + orderDTO.getId());
        orderEventDto.setStatus("pending");
        orderProducer.sendMessage(orderEventDto);

        return orderResponse;
    }

    @PutMapping("/updateorder")
    public OrderDTO updateOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(orderDTO);
    }

    @DeleteMapping("/deleteorder/{orderId}")
    public String deleteOrder(@PathVariable Integer orderId) {
        return orderService.deleteOrder(orderId);
    }
}
