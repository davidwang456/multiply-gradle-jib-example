package com.example.serviceb.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serviceb.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
}

