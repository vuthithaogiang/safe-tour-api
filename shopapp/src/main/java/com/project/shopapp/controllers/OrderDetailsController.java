package com.project.shopapp.controllers;


import com.project.shopapp.dtos.OrderDetailsDTO;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
public class OrderDetailsController {

    @PostMapping()
    public ResponseEntity<?> createOrderDetails(@Valid @RequestBody OrderDetailsDTO orderDetailsDTO,
                                                BindingResult result) {
        try{

            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }

            return ResponseEntity.ok("Insert order details success");

        }

        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok("Get order details has id: " + id);
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@Valid @PathVariable("order_id") Long orderId) {
        return ResponseEntity.ok("Get order details with order id = " + orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetails(@Valid @PathVariable("id") Long id,
                                                @Valid @RequestBody OrderDetailsDTO orderDetailsDTO,
                                                BindingResult result){
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }

            return ResponseEntity.ok("Update order details' id = " + id);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
