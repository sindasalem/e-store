package com.quest.etna.service;

import com.quest.etna.model.Order;
import com.quest.etna.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Transactional
public class OrderService {

    @Autowired
    public OrderRepository orderRepository;

    public Iterable<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }


    public Order create(Order order) {
        order.setDateCreated(LocalDate.now());
        return this.orderRepository.save(order);
    }

    public void update(Order order) {
         this.orderRepository.save(order);
    }
}
