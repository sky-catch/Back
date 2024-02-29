package com.example.api.chat.config;

import com.example.api.chat.ChatService;
import com.example.api.chat.dto.Chat;
import com.example.api.chat.dto.ChatRoomSession;
import com.example.core.exception.SystemException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private static final Gson gson = new Gson();
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        Chat chat = gson.fromJson(message.getPayload(), Chat.class);

        //db 저장
        //todo 개발 후 주석 해제
        //chatService.createChat(chat.getChatRoomId(), chat.getMasterId());

        //채팅방의 다른 사람한테 채팅 보내기
        ChatRoomSession chatRoomSession = enableChatRooms.get(chat.getChatRoomId());
        Set<WebSocketSession> theOtherSession = chatRoomSession.getTheOther(session);

        try {
            for (WebSocketSession targetSession : theOtherSession) {
                if (targetSession.isOpen()) {
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
        log.info(session + " 클라이언트 접속");
        String header = session.getHandshakeHeaders().getFirst("chatRoomId");

        if(StringUtils.isEmpty(header)){
            throw new SystemException("헤더를 추가해주세요");
        }
        long chatRoomId = Long.parseLong(header);

        ChatRoomSession chatRoom = enableChatRooms.getOrDefault(chatRoomId, new ChatRoomSession());
        chatRoom.addSession(session);
        enableChatRooms.put(chatRoomId, chatRoom);
    }

    /* Client가 접속 해제 시 호출되는 메서드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info(session + " 클라이언트 접속 해제");
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
