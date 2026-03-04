package com.example.serviceb.controller;

import com.example.common.order.response.OrderResponse;
import com.example.common.order.util.OrderConverter;
import com.example.serviceb.service.OrderQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    public OrderQueryController(OrderQueryService orderQueryService) {
        this.orderQueryService = orderQueryService;
    }

    @GetMapping
    public List<OrderResponse> list() {
        return orderQueryService.listOrders().stream()
                .map(OrderConverter::toResponse)
                .collect(Collectors.toList());
    }
}

