package com.weiss.weiss.services;

import com.google.common.io.BaseEncoding;
import com.weiss.weiss.model.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Component
public class TokenHandler {
    private final SecretKey secretKey;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public TokenHandler( ) {
        String jwsKey = "somerandomString123456";
        byte [] decodedKey = BaseEncoding.base64().decode(jwsKey);
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public Optional<String> extractUserId(@NonNull String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            return Optional.ofNullable(body.getId());
        }catch (Exception e ){
           throw new BadCredentialsException("token not valid");
        }
    }

    public String generateTokenId(@NonNull String id, @NonNull LocalDateTime localDateTime) {
        return Jwts.builder()
                .setId(""+id)
                .setExpiration(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(secretKey)
                .compact();
    }
    public boolean checkMatchesPasswords(UserInfo savedUser, UserInfo loginUser){
        return passwordEncoder.matches(loginUser.getPassword(),savedUser.getPassword());
    }

}
