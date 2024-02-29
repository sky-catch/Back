package com.example.api.chat.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomSession {

    private Set<WebSocketSession> sessions = new HashSet<>();

    public Set<WebSocketSession> getTheOther(WebSocketSession session) {
        Set<WebSocketSession> others = new HashSet<>(sessions);
        others.remove(session);
        return others;
    }

    public void addSession(WebSocketSession session){
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session){
        sessions.remove(session);
    }

    public boolean isEmpty(){
        return sessions.isEmpty();
    }

}
