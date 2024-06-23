package com.project.mychat.service;

import com.project.mychat.entity.Message;
import com.project.mychat.entity.User;
import com.project.mychat.exception.ChatException;
import com.project.mychat.exception.MessageException;
import com.project.mychat.exception.UserException;
import com.project.mychat.request.SendMessageRequest;

import java.util.List;

public interface MessageService {
    public Message sendMessage(SendMessageRequest request) throws UserException, ChatException;
    public List<Message> getChatsMessage(Integer chatId, User requestUser) throws ChatException, UserException;
    public Message findMessageById(Integer messageId) throws MessageException;
    public void deleteMessage(Integer messageId, User requestUser) throws MessageException, UserException;

}
