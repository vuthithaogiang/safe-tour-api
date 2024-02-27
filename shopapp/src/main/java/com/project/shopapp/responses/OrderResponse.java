package com.project.shopapp.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse extends BaseResponse {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String address;

    private String note;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("order_date")
    private Date orderDate;

    private String status;


    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;


    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("tracking_number")
    private  String trackingNumber;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private Boolean active;


}
