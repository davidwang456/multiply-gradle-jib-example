package com.example.servicea.service.impl;

import com.example.common.order.dto.OrderDto;
import com.example.common.order.pojo.Order;
import com.example.common.order.request.CreateOrderRequest;
import com.example.common.order.util.OrderConverter;
import com.example.servicea.entity.OrderEntity;
import com.example.servicea.repository.OrderMapper;
import com.example.servicea.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {
        Order order = OrderConverter.fromCreateRequest(request);
        OrderEntity entity = toEntity(order);
        orderMapper.insert(entity);
        order.setId(entity.getId());
        return OrderConverter.toDto(order);
    }

    @Override
    public List<OrderDto> listOrders() {
        List<OrderEntity> entities = orderMapper.selectList(null);
        return entities.stream()
                .map(this::toOrder)
                .map(OrderConverter::toDto)
                .collect(Collectors.toList());
    }

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setOrderNo(order.getOrderNo());
        entity.setUserId(order.getUserId());
        entity.setAmount(order.getAmount());
        entity.setCreatedAt(order.getCreatedAt());
        return entity;
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

