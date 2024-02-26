package com.project.shopapp.services;


import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    @Override
    public User register(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();

        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number has existed.");
        }


        User newUser = User
                   .builder()
                   .fullName(userDTO.getFullName())
                   .address(userDTO.getAddress())
                   .phoneNumber(userDTO.getPhoneNumber())
                   .password(userDTO.getPassword())
                   .dateOfBirth(userDTO.getDateOfBirth())
                   .facebookAccountId(userDTO.getFacebookAccountId())
                   .googleAccountId(userDTO.getGoogleAccountId())
                   .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found."));

        newUser.setRole(role);

        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
          //  String encodePassword = passwordEncoder.encode(password);
          //  newUser.setPassword(encodePassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
