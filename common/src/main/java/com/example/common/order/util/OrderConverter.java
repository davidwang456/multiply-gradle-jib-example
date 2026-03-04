package com.example.common.order.util;

import com.example.common.order.pojo.Order;
import com.example.common.order.dto.OrderDto;
import com.example.common.order.request.CreateOrderRequest;
import com.example.common.order.response.OrderResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderConverter {

    public static Order fromCreateRequest(CreateOrderRequest request) {
        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString());
        order.setUserId(request.getUserId());
        order.setAmount(request.getAmount());
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }

    public static OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setUserId(order.getUserId());
        dto.setAmount(order.getAmount());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    public static OrderResponse toResponse(OrderDto dto) {
        OrderResponse resp = new OrderResponse();
        resp.setId(dto.getId());
        resp.setOrderNo(dto.getOrderNo());
        resp.setUserId(dto.getUserId());
        resp.setAmount(dto.getAmount());
        resp.setCreatedAt(dto.getCreatedAt());
        return resp;
    }
}

