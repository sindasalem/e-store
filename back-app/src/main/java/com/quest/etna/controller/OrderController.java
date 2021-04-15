package com.quest.etna.controller;

import com.quest.etna.dto.OrderProductDto;
import com.quest.etna.model.Order;
import com.quest.etna.model.OrderProduct;
import com.quest.etna.model.User;
import com.quest.etna.service.OrderProductService;
import com.quest.etna.service.OrderService;
import com.quest.etna.service.ProductService;
import com.quest.etna.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    public ProductService productService;
    @Autowired
    public OrderService orderService;
    @Autowired
    OrderProductService orderProductService;
    @Autowired
    UserService userService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Order> list() {
        return this.orderService.getAllOrders();
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody List<OrderProductDto> form) throws Exception {
        System.out.println();
        List<OrderProductDto> formDtos = form;
        Order order = new Order();
        order.setStatus("PAID");
        order = this.orderService.create(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductDto dto : formDtos) {
            orderProducts.add(orderProductService.create(new OrderProduct(order, productService.getProduct(dto.getProduct().getId()), dto.getQuantity())));
        }

        order.setOrderProducts(orderProducts);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = this.userService.findUserByUsername(username);
        Set<Order> userOrders= user.getOrders();
        userOrders.add(order);
        userService.save(user);

        order.setUser(user);

        this.orderService.update(order);

        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/orders/{id}")
                .buildAndExpand(order.getId())
                .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(order, headers, HttpStatus.CREATED);
    }


}