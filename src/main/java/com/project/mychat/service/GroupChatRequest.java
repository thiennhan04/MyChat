package com.project.mychat.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatRequest {
    private List<Integer> userIds;
    private String chat_name;
    private String chat_image;
}
