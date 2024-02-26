package com.project.shopapp.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {

    private Long id;
    private String name;
    private String thumbnail;
    private String description;
    private Float price;

    @JsonProperty("category_id")
    private Long categoryId;


}
