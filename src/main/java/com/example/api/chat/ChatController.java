package com.example.api.chat;

import com.example.api.chat.dto.GetChatRoom;
import com.example.api.chat.dto.GetChatRoomListRes;
import com.example.api.chat.dto.PostChatRoomRes;
import com.example.api.member.MemberDTO;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "채팅")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅방 생성")
    @PostMapping("/{restaurantId}")
    public PostChatRoomRes createChatRoom(@PathVariable long restaurantId, @LoginMember MemberDTO memberDTO) {
        long chatRoomId = chatService.createChatRoom(restaurantId, memberDTO.getMemberId());
        return new PostChatRoomRes(chatRoomId);
    }

    @Operation(summary = "채팅방 목록 보기")
    @GetMapping("/roomList")
    public List<GetChatRoomListRes> getChatRoomList(@LoginMember MemberDTO memberDTO) {
        return chatService.getChatRoomList(memberDTO.getMemberId());
    }

    @Operation(summary = "채팅방 보기")
    @GetMapping("/{chatRoomId}")
    public GetChatRoom getChatRoom(@PathVariable long chatRoomId) {
        return chatService.getChatRoom(chatRoomId);
    }

}
