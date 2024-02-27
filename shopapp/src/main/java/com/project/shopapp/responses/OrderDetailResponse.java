package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.OrderDetail;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {

    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("number_of_products")
    private int numberOfProducts;

    private  float price;

    @JsonProperty("total_money")
    private float totalMoney;

    private String color;

    public static OrderDetailResponse convertFromOrderDetail(OrderDetail orderDetail) {

              return  OrderDetailResponse.builder()
                        .id(orderDetail.getId())
                        .orderId(orderDetail.getOrder().getId())
                        .productId(orderDetail.getProduct().getId())
                        .price(orderDetail.getPrice())
                        .totalMoney(orderDetail.getTotalMoney())
                        .color(orderDetail.getColor())
                        .build();

    }

}
