package com.project.mychat.controller;

import com.project.mychat.entity.Message;
import com.project.mychat.entity.User;
import com.project.mychat.exception.ChatException;
import com.project.mychat.exception.MessageException;
import com.project.mychat.exception.UserException;
import com.project.mychat.request.SendMessageRequest;
import com.project.mychat.response.ApiResponse;
import com.project.mychat.service.MessageService;
import com.project.mychat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/api/message")
public class MessageController {
    private MessageService messageService;
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(
            @RequestBody SendMessageRequest request,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        request.setUserId(user.getId());
        Message message = messageService.sendMessage(request);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
    @PostMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatsMessageHandler(
            @RequestBody Integer chatId,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        List<Message> messages = messageService.getChatsMessage(chatId, user);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(
            @RequestBody Integer messageId,
            @RequestHeader("Authorization") String jwt) throws UserException, ChatException, MessageException {
        User user = userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId, user);
        ApiResponse res = new ApiResponse("Message deleted successfully", false);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
