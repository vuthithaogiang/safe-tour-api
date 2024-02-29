package com.project.shopapp.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Category;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertCategoryResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("category")
    private Category category;


    @JsonProperty("errors")
    private List<String> errors;
}
