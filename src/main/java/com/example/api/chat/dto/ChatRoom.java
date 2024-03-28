package com.example.api.chat.dto;

import com.example.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseDTO {

    private long chatRoomId;
    private long memberId;
    private long restaurantId;
    private long ownerId;

    public ChatRoom(long memberId, long restaurantId) {
        this.memberId = memberId;
        this.restaurantId = restaurantId;
    }
}
