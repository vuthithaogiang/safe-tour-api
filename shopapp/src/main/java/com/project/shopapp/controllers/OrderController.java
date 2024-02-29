package com.project.shopapp.controllers;


import com.project.shopapp.components.LocalizationUtil;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.responses.*;
import com.project.shopapp.services.IOrderService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private  final LocalizationUtil localizationUtil;

    @PostMapping("")
    public ResponseEntity<InsertOrderResponse> insertOrder(@Valid @RequestBody OrderDTO orderDTO,
                                                           BindingResult result){
        try{

            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(
                        InsertOrderResponse
                                .builder()
                                .message(localizationUtil.getLocalizationMessage(MessageKeys.ORDER_CREATE_FAILED))
                                .errors(errorMessages)
                                .build()
                );
            }

            OrderResponse orderResponse  =  orderService.createOrder(orderDTO);
            return ResponseEntity.ok(
                    InsertOrderResponse
                            .builder()
                            .message(localizationUtil.getLocalizationMessage(MessageKeys.ORDER_CREATE_SUCCESSFULLY))
                            .order(orderResponse)
                            .build()
            );
        }

        catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.badRequest().body(
                    InsertOrderResponse
                            .builder()
                            .message(localizationUtil.getLocalizationMessage(MessageKeys.ORDER_CREATE_FAILED))
                            .errors(errors)
                            .build()
            );
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
    public ResponseEntity<UpdateOrderResponse> updateOrder( @PathVariable long id, @Valid @RequestBody OrderDTO orderDTO,
                                          BindingResult result) {
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(
                        UpdateOrderResponse
                                .builder()
                                .message(localizationUtil.getLocalizationMessage(MessageKeys.ORDER_UPDATE_FAILED, id))
                                .errors(errorMessages)
                                .build()
                );
            }
            OrderResponse orderResponse = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(
                    UpdateOrderResponse
                            .builder()
                            .message(localizationUtil.getLocalizationMessage(MessageKeys.ORDER_UPDATE_SUCCESSFULLY, id))
                            .order(orderResponse)
                            .build()
            );

        }

        catch (Exception e) {
           List<String> errors = new ArrayList<>();
           errors.add((e.getMessage()));
            return ResponseEntity.badRequest().body(
                    UpdateOrderResponse
                            .builder()
                            .message(localizationUtil.getLocalizationMessage(MessageKeys.ORDER_UPDATE_FAILED, id))
                            .errors(errors)
                            .build()
            );

        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteOrderResponse> deleteOrder(@PathVariable("id") Long id) {
        try{
            orderService.deleteOrder(id);
            return ResponseEntity.ok(
                    DeleteOrderResponse
                            .builder()
                            .message(localizationUtil.getLocalizationMessage(MessageKeys.ORDER_DELETE_SUCCESSFULLY, id))
                            .build()
            );
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    DeleteOrderResponse
                            .builder()
                            .message(localizationUtil.getLocalizationMessage(MessageKeys.ORDER_DELETE_FAILED, id))
                            .build()
            );
        }
    }

}
