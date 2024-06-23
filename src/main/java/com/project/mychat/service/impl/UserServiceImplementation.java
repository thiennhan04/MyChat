package com.project.mychat.service.impl;

import com.project.mychat.config.TokenProvider;
import com.project.mychat.entity.User;
import com.project.mychat.exception.UserException;
import com.project.mychat.repository.UserRepository;
import com.project.mychat.request.UpdateUserRequest;
import com.project.mychat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private TokenProvider tokenProvider;
    @Override
    public User findUserById(Integer id) throws UserException {
        Optional<User> opUser = userRepository.findById(id);
        if(opUser.isPresent()){
            return opUser.get();
        }
        throw new UserException("User not found id + " + id);
    }
    @Override
    public User findUserProfile(String jwt) throws UserException{
        String email = tokenProvider.getEmailFromToken(jwt);
        if(email != null){
            throw new BadCredentialsException("recieved invalid token---");
        }
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("user not found with email " + email);
        }
        return user;
    }

    @Override
    public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
        User user = findUserById(userId);
        if(req.getFullName() != null){
            user.setFull_name(req.getFullName());
        }
        if (req.getProfile_picture() != null){
            user.setProfile_picture(req.getProfile_picture());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        List<User> users = userRepository.searchUser(query);
        return users;
    }
}
