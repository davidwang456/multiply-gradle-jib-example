package com.example.servicea.service;

import com.example.common.order.dto.OrderDto;
import com.example.common.order.request.CreateOrderRequest;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequest request);

    List<OrderDto> listOrders();
}

