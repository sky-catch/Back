package com.example.api.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "식당 이름")
    private String restaurantName;
    @Schema(description = "식당 이미지 url")
    private String restaurantImage;
    @Schema(description = "마지막 채팅 날짜")
    private LocalDateTime lastChatDate;
    @Schema(description = "마지막 채팅 내용")
    private String lastChat;
    @Schema(description = "새로운 채팅이 있는지(있으면 true)")
    private boolean hasNewChat;
    @Schema(description = "채팅방 생성 날짜")
    private LocalDateTime createdDate;

}
