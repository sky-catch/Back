package com.example.api.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Chat{

    private long chatRoomId;
    private long masterId;
    private boolean readChat;
    private String content;

}
