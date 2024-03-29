package com.project.shopapp.services;


import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IUserService {
    User register(UserDTO userDTO) throws Exception ;

    String login(UserLoginDTO userLoginDTO) throws Exception;


    List<User> getAllUsers();




}
