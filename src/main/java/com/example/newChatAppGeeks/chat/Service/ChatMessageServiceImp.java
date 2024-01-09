package com.example.newChatAppGeeks.chat.Service;


import com.example.newChatAppGeeks.chat.Repository.ChatMessageRepository;
import com.example.newChatAppGeeks.chat.Model.ChatMessage;
import com.example.newChatAppGeeks.chattingRoom.Service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImp implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        var chatId = this.chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        this.chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = this.chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(this.chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }
}
