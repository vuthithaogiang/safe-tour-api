package com.project.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @JsonProperty("user_id")
    @Min(value = 1, message = "User's Id mus be greater than 0")
    private Long userId;

    @JsonProperty("full_name")
    private String fullName;

    private String email;


    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 9, max = 11, message = "Phone number have to length 9-11 characters")
    private String phoneNumber;

    @NotBlank(message = "Address must be required")
    private String address;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be >= 0")
    private Float totalMoney;


    @JsonProperty("shipping_method")
    private  String shippingMethod;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("shipping_address")
    private  String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;



}
