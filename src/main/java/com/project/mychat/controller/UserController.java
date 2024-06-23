package com.project.mychat.controller;

import com.project.mychat.entity.User;
import com.project.mychat.exception.UserException;
import com.project.mychat.request.UpdateUserRequest;
import com.project.mychat.response.ApiResponse;
import com.project.mychat.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    private UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandle(@RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserProfile(token);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }
    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String  q){
        List<User> users = userService.searchUser(q);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler(
            @RequestBody UpdateUserRequest req,
            @RequestHeader("Authorization") String token) throws UserException, ExecutionControl.UserException {
            User user =  userService.findUserProfile(token);
            userService.updateUser(user.getId(), req);
            ApiResponse res = new ApiResponse("user updated successfully", true);
            return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }
}
