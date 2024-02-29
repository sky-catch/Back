package com.example.api.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMapper chatMapper;

    public void createChat(long chatRoomId, long masterId) {
        //todo 채팅방이 있는지 검사

        chatMapper.createChat(chatRoomId, masterId);
    }
}
