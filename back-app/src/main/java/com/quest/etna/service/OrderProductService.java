package com.quest.etna.service;

import com.quest.etna.model.OrderProduct;
import com.quest.etna.repositories.OrderProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class OrderProductService {

    @Autowired
    private OrderProductRepository orderProductRepository;


    public OrderProduct create(OrderProduct orderProduct) {
        return this.orderProductRepository.save(orderProduct);
    }
}