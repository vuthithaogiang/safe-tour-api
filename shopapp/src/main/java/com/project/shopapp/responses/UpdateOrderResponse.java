package com.project.shopapp.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("order")
    private OrderResponse order;

    @JsonProperty("errors")
    private List<String> errors;

}
