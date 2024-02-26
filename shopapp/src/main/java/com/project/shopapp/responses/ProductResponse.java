package com.project.shopapp.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
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

    public static ProductResponse convertFromProduct(Product product)  {
        ProductResponse productResponse  =  ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .build();

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;
    }


}
