package com.example.api.chat;

import com.example.api.chat.dto.Chat;
import com.example.api.chat.dto.ChatRoom;
import com.example.api.chat.dto.GetChatRoom;
import com.example.api.chat.dto.GetChatRoomListRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {

    boolean isExistChatRoom(long chatRoomId);

    void createChat(Chat chat);

    void createChatRoom(ChatRoom chatRoom);

    List<GetChatRoomListRes> getChatRoomList(long memberId);

    GetChatRoom getChatRoom(long chatRoomId);

    void readChat(long chatRoomId, boolean memberChat);

}
