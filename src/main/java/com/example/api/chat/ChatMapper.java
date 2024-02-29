package com.example.api.chat;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

    void createChat(long chatRoomId, long masterId);
}
