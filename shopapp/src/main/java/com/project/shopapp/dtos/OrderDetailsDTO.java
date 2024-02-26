package com.project.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {


    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's id must be > 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's id must be > 0")
    private Long productId;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "The number of product must be >= 1")
    private  int numberOfProducts;

    @Min(value = 0, message = "Price must be >= 0")
    private  float price;

    @Min(value = 0, message = "Total price must be >= 0")
    @JsonProperty("total_money")
    private float totalMoney;


    private  String color;
}
