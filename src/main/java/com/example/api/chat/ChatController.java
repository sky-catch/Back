package com.example.api.chat;

import com.example.api.chat.dto.GetChatRoom;
import com.example.api.chat.dto.GetChatRoomListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "채팅")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅방 생성")
    @PostMapping("/{restaurantId}")
    public void createChatRoom(@PathVariable long restaurantId){
        chatService.createChatRoom(restaurantId, 1L);
    }

    @Operation(summary = "채팅방 목록 보기")
    @GetMapping("/roomList")
    public List<GetChatRoomListRes> getChatRoomList(){
        return chatService.getChatRoomList(1L);
    }

    @Operation(summary = "채팅방 보기")
    @GetMapping("/{chatRoomId}")
    public GetChatRoom getChatRoom(@PathVariable long chatRoomId){
        return chatService.getChatRoom(chatRoomId);
    }

}
