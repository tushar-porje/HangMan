package com.hangman.hangman_apis.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hangman.hangman_apis.dto.UserDto;
import com.hangman.hangman_apis.entity.User;
import com.hangman.hangman_apis.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // User getUserFromUserDto(UserDto userDto){
    //     return modelMapper.map(userDto,User.class);
    // }
}
