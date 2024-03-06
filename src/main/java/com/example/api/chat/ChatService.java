package com.example.api.chat;

import com.example.api.chat.dto.Chat;
import com.example.api.chat.dto.GetChatRoom;
import com.example.api.chat.dto.GetChatRoomListRes;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMapper chatMapper;

    public void createChat(Chat chat) {
        if(!chatMapper.isExistChatRoom(chat.getChatRoomId())){
            throw new SystemException("채팅방이 없습니다.");
        }

        chatMapper.createChat(chat);
    }

    public void createChatRoom(long restaurantId, long memberId) {
        chatMapper.createChatRoom(restaurantId, memberId);
    }

    public List<GetChatRoomListRes> getChatRoomList(long memberId) {
        return chatMapper.getChatRoomList(memberId);
    }

    public GetChatRoom getChatRoom(long chatRoomId) {
        return chatMapper.getChatRoom(chatRoomId);
    }

    public void readChat(long chatRoomId, boolean memberChat){
        chatMapper.readChat(chatRoomId, memberChat);
    }
}
