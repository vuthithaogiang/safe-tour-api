package com.project.shopapp.controllers;


import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.LoginResponse;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.utils.LocalizationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private  final LocalizationUtil localizationUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO,
                                      BindingResult result) {

        try {

            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages);
            }

            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Password does not match");
            }
            User user = userService.register(userDTO);
           // return ResponseEntity.ok("Register successfully");
            return ResponseEntity.ok(user);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                   HttpServletRequest request) {
        try{
            String token = userService.login(userLoginDTO);

            LoginResponse loginResponse = LoginResponse
                    .builder()
                    .token(token)
                    .message(localizationUtil.getLocalizationMessage("user.login.login_successfully", request))
                    .build();
            return ResponseEntity.ok(loginResponse);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                            .token(null)
                            .message(e.getMessage())

                    .build());
        }


    }

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok("Get users");
    }

}
