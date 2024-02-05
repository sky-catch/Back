package com.example.core.web.security.jwt;

import com.example.api.member.MemberDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JWTProvider {

    @Value("${jwt.secret}")
    private byte[] secretCode;
    private Key key;
    private static final long expirationDate = 1000L * 60 * 60 * 24; //현재 24시간 (1000 = 1초)

    public String createToken(MemberDTO memberDTO) {
        key = Keys.hmacShaKeyFor(secretCode);

        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("memberId", memberDTO.getMemberId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationDate))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public long getMemberId(String token){
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return claims.getBody().get("memberId", Long.class);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
    }

}
