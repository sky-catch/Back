package com.example.api.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetChatRoomListRes {

    private long chatRoomId;
    private String restaurantName;
    private String restaurantImage;
    private LocalDateTime lastChatDate;
    private String lastChat;
    private boolean hasNewChat;
    private LocalDateTime createdDate;

}
