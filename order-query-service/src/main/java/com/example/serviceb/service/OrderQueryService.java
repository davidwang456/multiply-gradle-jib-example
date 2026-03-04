package com.example.serviceb.service;

import com.example.common.order.dto.OrderDto;

import java.util.List;

public interface OrderQueryService {

    List<OrderDto> listOrders();
}

