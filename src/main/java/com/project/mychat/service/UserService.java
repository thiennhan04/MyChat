package com.project.mychat.service;

import com.project.mychat.entity.User;
import com.project.mychat.exception.UserException;
import com.project.mychat.request.UpdateUserRequest;
import jdk.jshell.spi.ExecutionControl;

import java.util.List;

public interface UserService {
    public User findUserById(Integer id)  throws UserException;
    public User findUserProfile(String jwt) throws UserException;
    public User updateUser(Integer userId, UpdateUserRequest updateUserRequest) throws ExecutionControl.UserException, UserException;
    public List<User> searchUser(String query);

}
