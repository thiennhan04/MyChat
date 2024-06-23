package com.project.mychat.service;

import com.project.mychat.entity.Chat;
import com.project.mychat.entity.User;
import com.project.mychat.exception.ChatException;
import com.project.mychat.exception.UserException;

import java.util.List;

public interface ChatService {
    public Chat createChat(User requestUser, Integer userId) throws UserException;
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException;
    public Chat findChatById(Integer chatId) throws ChatException;
    public Chat createGroup(GroupChatRequest groupChatRequest, User requestUser) throws UserException;
    public Chat addUserToGroup(Integer userId, Integer chatId, User requestUser) throws UserException, ChatException;
    public Chat renameGroup(Integer chatId, String groupName, User requestUser) throws ChatException, UserException;
    public Chat removeFromGroup(Integer chatId, Integer userId, User requestUser) throws UserException, ChatException;
    public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException;

}
