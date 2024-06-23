package com.project.mychat.controller;

import com.project.mychat.entity.Chat;
import com.project.mychat.entity.User;
import com.project.mychat.exception.ChatException;
import com.project.mychat.exception.UserException;
import com.project.mychat.request.SingleChatRequest;
import com.project.mychat.response.ApiResponse;
import com.project.mychat.service.ChatService;
import com.project.mychat.service.GroupChatRequest;
import com.project.mychat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private ChatService chatService;
    private UserService userService;
    public ChatController(ChatService chatService, UserService  userService){
        this.chatService = chatService;
        this.userService = userService;
    }
    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(
             @PathVariable Integer chatId,
             @RequestHeader("Authorization")String jwt) throws UserException, ChatException {
        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }
    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(
            @RequestBody SingleChatRequest singleChatRequest,
            @RequestHeader("Authorization")String jwt) throws UserException {
        User requestUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createChat(requestUser, singleChatRequest.getUserId());
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }
    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(
            @RequestBody GroupChatRequest groupChatRequest,
            @RequestHeader("Authorization")String jwt) throws UserException {
        User requestUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createGroup(groupChatRequest,requestUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserIdHandler(
             @RequestHeader("Authorization")String jwt) throws UserException {
        User requestUser = userService.findUserProfile(jwt);
        List<Chat> chats = chatService.findAllChatByUserId(requestUser.getId());
        return new ResponseEntity<List<Chat>>(chats, HttpStatus.OK);
    }
    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(
            @PathVariable Integer chatId,
            @PathVariable Integer userId,
            @RequestHeader("Authorization")String jwt ) throws UserException, ChatException {
        User requestUser = userService.findUserProfile(jwt);
        Chat chats = chatService.addUserToGroup(userId, chatId, requestUser);
        return new ResponseEntity<Chat>(chats, HttpStatus.OK);
    }
    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromGroupHandler(
            @PathVariable Integer chatId,
            @PathVariable Integer userId,
            @RequestHeader("Authorization")String jwt ) throws UserException, ChatException {
        User requestUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeFromGroup(chatId, userId, requestUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(
            @PathVariable Integer chatId,
            @RequestHeader("Authorization")String jwt ) throws UserException, ChatException {
        User requestUser = userService.findUserProfile(jwt);
        chatService.deleteChat(chatId, requestUser.getId());
        ApiResponse res = new ApiResponse("Chat is deleted successfully", true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
