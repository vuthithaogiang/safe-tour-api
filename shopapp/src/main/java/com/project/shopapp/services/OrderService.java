package com.project.shopapp.services;


import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final   UserRepository userRepository;
    private final   ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception{
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() ->
                        new DataNotFoundException("Not Found user id: " +
                                orderDTO.getUserId()));

        // Convert orderDTO -> order => insert into DB
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings((mapper) -> {
                    mapper.skip(Order::setId);
                });

        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        //Shipping Date >= Today
        LocalDate shippingDate = orderDTO.getShippingDate() == null ?
                LocalDate.now() : orderDTO.getShippingDate();

        if( shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);

        modelMapper.typeMap(Order.class, OrderResponse.class);
        OrderResponse orderResponse = new OrderResponse();

        modelMapper.map(order, orderResponse);


        return orderResponse;
    }

    @Override
    public OrderResponse getOrderById(Long id) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                       new DataNotFoundException("Not found order details with id: " + id));
        modelMapper.typeMap(Order.class, OrderResponse.class);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(order, orderResponse);

        return orderResponse;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws Exception {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Not Found order with id: " + id));

       User existingUser = userRepository.findById(orderDTO.getUserId())
               .orElseThrow(() ->
                       new DataNotFoundException("Not Found user id: " + orderDTO.getUserId()));

       modelMapper.typeMap(OrderDTO.class, Order.class)
               .addMappings(mapper -> mapper.skip(Order::setId));

       modelMapper.map(orderDTO, existingOrder);
       existingOrder.setUser(existingUser);

       orderRepository.save(existingOrder);

       modelMapper.typeMap(Order.class, OrderResponse.class);
       OrderResponse orderResponse = new OrderResponse();
       modelMapper.map(existingOrder, orderResponse);
        return orderResponse;
    }

    @Override
    public void deleteOrder(Long id) {
        Order optionalOrder = orderRepository.findById(id).orElse(null);

        if(optionalOrder != null) {
            optionalOrder.setActive(false);
            orderRepository.save(optionalOrder);
        }
    }

    @Override
    public List<OrderResponse> getAllOrders(Long userId) throws Exception {
        List<Order> orders = orderRepository.findByUserId(userId);

        modelMapper.typeMap(Order.class, OrderResponse.class);
        List<OrderResponse> orderResponseList = new ArrayList<>();

       if(orders.isEmpty()) {
           throw new DataNotFoundException("Not found list order of user id " + userId);
       }

       for (Order order : orders) {
               OrderResponse orderResponse = new OrderResponse();
               modelMapper.map(order, orderResponse);
               orderResponseList.add(orderResponse);
       }

        return orderResponseList;
    }
}
