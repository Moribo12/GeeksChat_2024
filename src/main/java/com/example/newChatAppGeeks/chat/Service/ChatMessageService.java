package com.example.newChatAppGeeks.chat.Service;

import com.example.newChatAppGeeks.chat.Model.ChatMessage;

import java.util.List;
public interface ChatMessageService {
    ChatMessage saveMessage(ChatMessage chatMessage);
    List<ChatMessage> findChatMessages(String senderId, String recipientId);
}
