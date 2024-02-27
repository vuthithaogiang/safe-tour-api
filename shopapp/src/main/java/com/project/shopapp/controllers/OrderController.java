package com.project.shopapp.controllers;


import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.responses.OrderResponse;
import com.project.shopapp.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> insertOrder(@Valid @RequestBody OrderDTO orderDTO,
                                        BindingResult result){

        try{

            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }

            OrderResponse orderResponse  =  orderService.createOrder(orderDTO);
            return ResponseEntity.ok(orderResponse);

        }

        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping ("/user/{user_id}")
    public ResponseEntity<?> getOrders(@PathVariable("user_id") Long userId) {
        try{
            List<OrderResponse> orderResponseList = orderService.getAllOrders(userId);
            return ResponseEntity.ok(orderResponseList);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") Long id) {
        try{
            OrderResponse orderDetails = orderService.getOrderById(id);
            return ResponseEntity.ok(orderDetails);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder( @PathVariable long id, @Valid @RequestBody OrderDTO orderDTO,
                                          BindingResult result) {
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderResponse orderResponse = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(orderResponse);

        }

        catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
        try{
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Delete order id: " + id + " successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
