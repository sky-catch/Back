package com.example.api.chat.config;

import com.example.api.chat.ChatService;
import com.example.api.chat.dto.Chat;
import com.example.api.chat.dto.ChatRoomSession;
import com.example.core.exception.SystemException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private static final Map<Long, ChatRoomSession> enableChatRooms = new HashMap<>();
    private final Gson gson = new Gson();
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        Chat chat = gson.fromJson(message.getPayload(), Chat.class);
        String memberChatHeader = session.getHandshakeHeaders().getFirst("memberChat");

        //채팅 읽음 체크
        chatService.readChat(chat.getChatRoomId(), chat.isMemberChat());

        //db 저장
        chatService.createChat(chat);

        //채팅방의 다른 사람한테 채팅 보내기
        ChatRoomSession chatRoomSession = enableChatRooms.get(chat.getChatRoomId());
        Set<WebSocketSession> theOtherSession = chatRoomSession.getTheOther(session);

        try {
            chat.setReadChat(!chat.isReadChat());
            for (WebSocketSession targetSession : theOtherSession) {
                if (targetSession.isOpen()) {
                    chatService.readChat(chat.getChatRoomId(), !chat.isMemberChat());
                    targetSession.sendMessage(new TextMessage(gson.toJson(chat)));
                }
            }
        } catch (IOException e) {
            throw new SystemException("웹소켓 전송 에러");
        }

    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String chatRoomHeader = session.getHandshakeHeaders().getFirst("chatRoomId");
        //todo 현재 회원인지 사장인지 어떤걸로 구분할지 정해지지 않아서 임시로 헤더로 구분
        String memberChatHeader = session.getHandshakeHeaders().getFirst("memberChat");

        if(StringUtils.isEmpty(chatRoomHeader) ||StringUtils.isEmpty(memberChatHeader)){
            throw new SystemException("헤더를 추가해주세요");
        }
        long chatRoomId = Long.parseLong(chatRoomHeader);
        boolean memberChat = Boolean.parseBoolean(memberChatHeader);

        ChatRoomSession chatRoom = enableChatRooms.getOrDefault(chatRoomId, new ChatRoomSession());
        chatRoom.addSession(session);
        enableChatRooms.put(chatRoomId, chatRoom);

        //채팅 읽음 체크
        chatService.readChat(chatRoomId, memberChat);
    }

    /* Client가 접속 해제 시 호출되는 메서드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String header = session.getHandshakeHeaders().getFirst("chatRoomId");

        if(StringUtils.isEmpty(header)){
            return;
        }
        long chatRoomId = Long.parseLong(header);

        if(!enableChatRooms.containsKey(chatRoomId)){
            return;
        }
        ChatRoomSession chatRoomSession = enableChatRooms.get(chatRoomId);
        chatRoomSession.removeSession(session);
        if (chatRoomSession.isEmpty()) {
            enableChatRooms.remove(chatRoomId);
        }
    }
}
