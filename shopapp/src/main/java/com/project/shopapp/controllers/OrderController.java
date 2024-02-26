package com.project.shopapp.controllers;


import com.project.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
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

            return ResponseEntity.ok("Create new order");

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
            return ResponseEntity.ok("Update order id: " + id);

        }

        catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }



}
