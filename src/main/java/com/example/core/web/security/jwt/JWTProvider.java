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

import static com.example.core.web.security.jwt.JWTUtils.TOKEN_PREFIX;

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
            throw new SystemException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new SystemException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new SystemException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
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

    public void validateBearerToken(String tokenHeader) {
        if (!tokenHeader.startsWith(TOKEN_PREFIX)) {
            throw new SystemException("토큰이 Bearer로 시작하지 않습니다.");
        }
        validateToken(tokenHeader.replace(TOKEN_PREFIX, ""));
    }
}
