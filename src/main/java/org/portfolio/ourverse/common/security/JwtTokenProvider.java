package org.portfolio.ourverse.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.portfolio.ourverse.common.constant.Authority;
import org.portfolio.ourverse.src.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final AuthService authService;
    @Value("${spring.jwt.secret}")
    private String secretKey;

    private static final String KEY_ROLE = "role";
    private static final String KEY_USERID = "userId";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
       토큰 생성
     */
    public String generateToken(String username, Authority role, Long userId) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLE, role);
        claims.put(KEY_USERID, userId); // pk

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME); // 만료 시간

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(this.getSigningKey())
                .compact();

    }

    /*
    토큰 만료일 확인
     */
    public boolean validateToken(String token) {
        if(!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);

        return claims.getExpiration().after(new Date()); // 만료일 확인
    }

    /*
    token 파싱
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /*
    token으로부터 userdetail 가져와 인증 정보 전달
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = authService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return parseClaims(token).getSubject();
    }
}
