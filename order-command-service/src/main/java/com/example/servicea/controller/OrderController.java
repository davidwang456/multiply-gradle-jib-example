package com.example.servicea.controller;

import com.example.common.order.request.CreateOrderRequest;
import com.example.common.order.response.OrderResponse;
import com.example.common.order.util.OrderConverter;
import com.example.servicea.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse create(@RequestBody CreateOrderRequest request) {
        return OrderConverter.toResponse(orderService.createOrder(request));
    }

    @GetMapping
    public List<OrderResponse> list() {
        return orderService.listOrders().stream()
                .map(OrderConverter::toResponse)
                .collect(Collectors.toList());
    }
}

