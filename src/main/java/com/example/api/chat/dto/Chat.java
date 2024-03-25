package com.example.api.chat.dto;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Chat extends BaseDTO {

    private long chatId;
    @JsonIgnore
    private long chatRoomId;
    @Schema(description = "회원의 채팅인지 true or false")
    private boolean memberChat;
    @Schema(description = "채팅을 읽었으면 true, 안 읽었으면 false")
    private boolean readChat;
    @Schema(description = "내용")
    private String content;

}
