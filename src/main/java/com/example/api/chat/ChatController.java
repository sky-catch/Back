package com.example.api.chat;

import com.example.api.chat.dto.GetChatRoom;
import com.example.api.chat.dto.GetChatRoomListRes;
import com.example.api.member.MemberDTO;
import com.example.api.owner.dto.Owner;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    public void createChatRoom(@PathVariable long restaurantId, @Parameter(hidden = true) @LoginMember MemberDTO memberDTO){
        chatService.createChatRoom(restaurantId, memberDTO.getMemberId());
    }

    @Operation(summary = "채팅방 목록 보기")
    @GetMapping("/roomList")
    public List<GetChatRoomListRes> getChatRoomList(@Parameter(hidden = true) @LoginMember MemberDTO memberDTO){
        return chatService.getChatRoomList(memberDTO.getMemberId());
    }

    @Operation(summary = "채팅방 보기")
    @GetMapping("/{chatRoomId}")
    public GetChatRoom getChatRoom(@PathVariable long chatRoomId){
        return chatService.getChatRoom(chatRoomId);
    }

}
