package com.project.shopapp.services;


import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private  final AuthenticationManager authenticationManager;

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
            String encodePassword = passwordEncoder.encode(password);
            newUser.setPassword(encodePassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) throws Exception{
        Optional<User> optionalUser = userRepository.findByPhoneNumber(userLoginDTO.getPhoneNumber());

        if(optionalUser.isEmpty()) {
            throw new
                    DataNotFoundException("Not found user, " +
                    "invalid phone number or password");
        }

        User existingUser = optionalUser.get();

        //check password
        if(existingUser.getFacebookAccountId() == 0 &&
        existingUser.getGoogleAccountId() == 0) {
            if(!passwordEncoder.matches(userLoginDTO.getPassword(),
                    existingUser.getPassword())){
                throw  new BadCredentialsException("Wrong phone number or password");
            }
        }


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getPhoneNumber(),
                        userLoginDTO.getPassword(),
                        existingUser.getAuthorities()
                );

        authenticationManager.authenticate(authenticationToken);


        //return jwt token
         return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
