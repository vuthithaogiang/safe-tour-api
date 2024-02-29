package com.project.shopapp.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;


    @JsonProperty("errors")
    private List<String> errors;
}
