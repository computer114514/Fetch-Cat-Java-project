package com.github.computer114514.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    public static final String secret="y2VlNqTh4l6J3xqDKEltncyqsiwZMnJ60uJZxx9qExK";

    public static final Long expiration=3600000L;

//    private static Key getSignKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes());
//    }
    private static Key getSignKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 验证token的有效性
     * @param token
     * @return
     */
    public static boolean validateToken(String token) {
        try{
//            Jwts.parser().setSigningKey(getSignKey()).build().parseSignedClaims(token);
            parseToken(token);
            return true;
        }catch(Exception e){
            return false;
        }

    }

    public static String generateToken(String username,Long userId){
        return Jwts.builder()
                .claim("userId",userId)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignKey())
                .compact();
    }
    public static Claims parseToken(String token){
        Claims claim = Jwts.parser()
                .verifyWith((SecretKey) getSignKey())          // 使用 verifyWith 替代 setSigningKey
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claim;
    }

}
