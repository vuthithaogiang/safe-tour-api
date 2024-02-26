package com.project.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {

    @JsonProperty(value = "product_id")
    @Min(value = 1, message = "Product's id must be >= 1")
    private Long productId;

    @JsonProperty(value = "image_url")
    @Size(min = 5, max = 200, message = "File image name mus be between 5 and 200 characters")
    private String imageUrl;
}
