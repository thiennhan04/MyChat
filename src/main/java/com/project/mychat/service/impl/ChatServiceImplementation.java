package com.project.mychat.service.impl;

import com.project.mychat.entity.Chat;
import com.project.mychat.entity.User;
import com.project.mychat.exception.ChatException;
import com.project.mychat.exception.UserException;
import com.project.mychat.repository.ChatRepository;
import com.project.mychat.service.ChatService;
import com.project.mychat.service.GroupChatRequest;
import com.project.mychat.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImplementation implements ChatService {
    private ChatRepository chatRepository;
    private UserService userService;
    public ChatServiceImplementation(ChatRepository chatRepository, UserService userService){
        this.chatRepository = chatRepository;
        this.userService = userService;
    }
    @Override
    public Chat createChat(User requestUser, Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        Chat isChatExist = chatRepository.findSingleChatByUserId(user, requestUser);
        if(isChatExist != null){
            return isChatExist;
        }
        Chat chat = new Chat();
        chat.setCreateBy(requestUser);
        chat.getUsers().add(user);
        chat.getUsers().add(requestUser);
        chat.setGroup(false);
        return chat;
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        List<Chat> chats = chatRepository.findChatByUserId(user.getId());
        return chats;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(chat.isPresent()){
            return chat.get();
        }
        throw new ChatException("Chat not found with id " + chatId);
    }

    @Override
    public Chat createGroup(GroupChatRequest groupChatRequest, User reqUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChat_image(groupChatRequest.getChat_image());
        group.setChat_name(groupChatRequest.getChat_name());
        group.setCreateBy(reqUser);
        group.getAdmins().add(reqUser);
        for(Integer userId : groupChatRequest.getUserIds()){
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }
        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User requestUser) throws UserException, ChatException {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        User user = userService.findUserById(userId);
        if(optionalChat.isPresent()){
            Chat chat = optionalChat.get();
            if(chat.getAdmins().contains(requestUser)){
                chat.getUsers().add(user);
                return chat;
            } else{
                throw new UserException("You are not admin");
            }

        }
        throw new ChatException("chat not found with id " + chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName,User requestUser) throws ChatException, UserException {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if(optionalChat.isPresent()){
            Chat chat = optionalChat.get();
            if(chat.getUsers().contains(requestUser)){
                chat.setChat_name(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException("you are not member of this group");
        }
        throw new ChatException("Chat not found with id " + chatId);
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, User requestUser) throws UserException, ChatException {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        User user = userService.findUserById(userId);
        if(optionalChat.isPresent()){
            Chat chat = optionalChat.get();
            if(chat.getAdmins().contains(requestUser)){
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            } else if(chat.getUsers().contains(requestUser)){
                if(user.getId().equals(requestUser.getId())){
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);
                }
            }
            throw new UserException("You can't remove another user");
        }
        throw new ChatException("Chat not found with id " + chatId);
    }

    @Override
    public void  deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {
        Optional<Chat>  optionalChat = chatRepository.findById(chatId);
        if(optionalChat.isPresent()){
             Chat chat = optionalChat.get();
             chatRepository.deleteById(chat.getId());
        }
    }
}
