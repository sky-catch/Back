package com.example.api.chat;

import com.example.api.chat.dto.Chat;
import com.example.api.chat.dto.GetChatRoom;
import com.example.api.chat.dto.GetChatRoomListRes;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMapper chatMapper;

    @Transactional
    public void createChat(Chat chat) {
        chatMapper.createChat(chat);
    }

    @Transactional(readOnly = true)
    public void checkExistChatRoom(long chatRoomId){
        if(!chatMapper.isExistChatRoom(chatRoomId)){
            throw new SystemException("채팅방이 없습니다.");
        }
    }

    @Transactional
    public void createChatRoom(long restaurantId, long memberId) {
        chatMapper.createChatRoom(restaurantId, memberId);
    }

    @Transactional(readOnly = true)
    public List<GetChatRoomListRes> getChatRoomList(long memberId) {
        return chatMapper.getChatRoomList(memberId);
    }

    @Transactional(readOnly = true)
    public GetChatRoom getChatRoom(long chatRoomId) {
        return chatMapper.getChatRoom(chatRoomId);
    }

    @Transactional
    public void readChat(long chatRoomId, boolean memberChat){
        chatMapper.readChat(chatRoomId, memberChat);
    }
}
