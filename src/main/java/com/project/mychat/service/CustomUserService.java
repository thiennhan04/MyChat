package com.project.mychat.service;

import com.project.mychat.entity.User;
import com.project.mychat.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserService  implements UserDetailsService {
    private UserRepository userRepository;
    public CustomUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public org.springframework.security.core.userdetails.User loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        User user = userRepository.findByEmail(username);
        if(user == null){
            throw  new UsernameNotFoundException("User not found with username: " + username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
