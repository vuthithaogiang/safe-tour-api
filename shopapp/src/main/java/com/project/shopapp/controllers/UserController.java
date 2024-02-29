package com.project.shopapp.controllers;


import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.LoginResponse;
import com.project.shopapp.responses.RegisterResponse;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.components.LocalizationUtil;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private  final LocalizationUtil localizationUtil;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody UserDTO userDTO,
                                                     BindingResult result) {

        try {

            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(RegisterResponse.builder()
                                .message(localizationUtil.getLocalizationMessage(MessageKeys.REGISTER_FAILED))
                                .errors(errorMessages)

                        .build());
            }

            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                List<String> errors = new ArrayList<>();
                String errorPassword = localizationUtil.getLocalizationMessage(MessageKeys.REGISTER_PASSWORD_NOT_MATCH);
                errors.add(errorPassword);


                return ResponseEntity.badRequest().body(RegisterResponse
                        .builder()
                                .message(localizationUtil.getLocalizationMessage(MessageKeys.REGISTER_FAILED))
                                .errors(errors)
                        .build());
            }
            User user = userService.register(userDTO);
           // return ResponseEntity.ok("Register successfully");
            return ResponseEntity.ok(RegisterResponse
                    .builder()
                    .message(localizationUtil.getLocalizationMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                    .user(user)
                    .build());
        }
        catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RegisterResponse
                            .builder()
                            .message(localizationUtil.getLocalizationMessage(MessageKeys.REGISTER_FAILED))
                            .errors(errors)
                            .build()
            );
        }

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO
                                   ) {
        try{
            String token = userService.login(userLoginDTO);

            LoginResponse loginResponse = LoginResponse
                    .builder()
                    .token(token)
                    .message(localizationUtil.getLocalizationMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                    .build();
            return ResponseEntity.ok(loginResponse);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse
                    .builder()
                    .message(localizationUtil.getLocalizationMessage
                            (MessageKeys.LOGIN_FAILED, e.getMessage()))
                    .build());
        }

    }

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok("Get users");
    }

}
