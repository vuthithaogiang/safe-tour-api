package com.project.shopapp.controllers;


import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetailsController {
    private final IOrderDetailService orderDetailService;

    @PostMapping()
    public ResponseEntity<?> createOrderDetails(@Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                                BindingResult result) {
        try{

            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }

           OrderDetail orderDetail =  orderDetailService.createOrderDetail(orderDetailDTO);

            return ResponseEntity.ok(OrderDetailResponse.convertFromOrderDetail(orderDetail));
        }

        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
        try{
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);

            return ResponseEntity.ok(OrderDetailResponse.convertFromOrderDetail(orderDetail));
        }

        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@Valid @PathVariable("order_id") Long orderId) {
        try{
            List<OrderDetail> orderDetails = orderDetailService.
                    findOrderDetailByOrderId(orderId);
            List<OrderDetailResponse> orderDetailResponses =
                    orderDetails
                            .stream()
                            .map(
                                 OrderDetailResponse::convertFromOrderDetail)
                            .toList();
            return ResponseEntity.ok(orderDetailResponses);

        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetails(@Valid @PathVariable("id") Long id,
                                                @Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                                BindingResult result){
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }
             OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok(orderDetail);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable("id") Long id) {
        try{
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok("Delete order detail id: " + id + " successfully");

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
