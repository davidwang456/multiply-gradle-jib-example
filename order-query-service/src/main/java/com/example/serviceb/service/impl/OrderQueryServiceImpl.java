package com.example.serviceb.service.impl;

import com.example.common.order.dto.OrderDto;
import com.example.common.order.pojo.Order;
import com.example.common.order.util.OrderConverter;
import com.example.serviceb.entity.OrderEntity;
import com.example.serviceb.repository.OrderMapper;
import com.example.serviceb.service.OrderQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderMapper orderMapper;

    public OrderQueryServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public List<OrderDto> listOrders() {
        List<OrderEntity> entities = orderMapper.selectList(null);
        return entities.stream()
                .map(this::toOrder)
                .map(OrderConverter::toDto)
                .collect(Collectors.toList());
    }

    private Order toOrder(OrderEntity entity) {
        Order order = new Order();
        order.setId(entity.getId());
        order.setOrderNo(entity.getOrderNo());
        order.setUserId(entity.getUserId());
        order.setAmount(entity.getAmount());
        order.setCreatedAt(entity.getCreatedAt());
        return order;
    }
}

