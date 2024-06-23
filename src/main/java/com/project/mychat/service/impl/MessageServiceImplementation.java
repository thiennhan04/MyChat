package com.project.mychat.service.impl;

import com.project.mychat.entity.Chat;
import com.project.mychat.entity.Message;
import com.project.mychat.entity.User;
import com.project.mychat.exception.ChatException;
import com.project.mychat.exception.MessageException;
import com.project.mychat.exception.UserException;
import com.project.mychat.repository.MessageRepository;
import com.project.mychat.request.SendMessageRequest;
import com.project.mychat.service.ChatService;
import com.project.mychat.service.MessageService;
import com.project.mychat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
public class MessageServiceImplementation implements MessageService {
    private MessageRepository messageRepository;
    private UserService userService;
    private ChatService chatService;

    @Override
    public Message sendMessage(SendMessageRequest request) throws UserException, ChatException {
        User user = userService.findUserById(request.getUserId());
        Chat chat = chatService.findChatById(request.getChatId());
        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());
        return message;
    }
    @Override
    public List<Message> getChatsMessage(Integer chatId, User requestUser) throws ChatException, UserException {
        Chat chat = chatService.findChatById(chatId);
        if(!chat.getUsers().contains(requestUser.getId())){
            throw new UserException("You are not releted to this chat " + chat.getId());
        }
        List<Message> messages = messageRepository.findByChatId(chat.getId());
        return messages;
    }
    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if(messageOptional.isPresent()){
            return messageOptional.get();
        }
        throw new MessageException("Message not found with id " + messageId);
    }
    @Override
    public void deleteMessage(Integer messageId, User requestUser) throws MessageException, UserException {
            Optional<Message>  messageOptional =  messageRepository.findById(messageId);
            if(messageOptional.isPresent()){
                Message message = messageOptional.get();
                if(message.getUser().getId().equals(requestUser.getId())){
                    messageRepository.deleteById(messageId);
                }
                throw new UserException("you cant delete another user's message " + requestUser.getFull_name());
            }
            throw new MessageException("Message not found with id " + messageId);

    }


}
