package com.project.shopapp.dtos;



import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    @NotBlank(message = "Name product is required")
    @Size(min = 3, max = 200, message = "Name mus be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 100000, message = "Price must be less than or equal to 100,000$")
    private Float price;

    private  String thumbnail;

    private String description;


    @NotNull(message = "Category id is required")
    @Min(value = 1 , message = "Category id must be >= 1")
    @JsonProperty("category_id")
    private Long categoryId;


}
