package com.example.core.web.security.jwt;

import com.example.core.exception.SystemException;
import com.example.core.web.security.dto.UsersDTO;
import com.example.core.web.security.jwt.dto.AccessToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JWTProvider {

    @Value("${jwt.secret}")
    private byte[] secretCode;
    private Key key;
    private static final long expirationDate = 1000L * 60 * 60 * 24; //현재 24시간 (1000 = 1초)

    public AccessToken createToken(UsersDTO usersDTO) {
        key = Keys.hmacShaKeyFor(secretCode);

        String accessToken = Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("email", usersDTO.getEmail())
                .claim("isOwner", usersDTO.isOwner())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationDate))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return AccessToken.builder()
                .value(accessToken)
                .build();
    }

    public String getMemberEmail(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return claims.getBody().get("email", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
            throw new SystemException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            throw new SystemException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            throw new SystemException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            throw new SystemException("JWT 토큰이 잘못되었습니다.");
        }
    }

    public boolean isOwner(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return claims.getBody().get("isOwner", Boolean.class);
    }
}
