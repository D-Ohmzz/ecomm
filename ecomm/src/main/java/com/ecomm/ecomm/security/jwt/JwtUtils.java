package com.ecomm.ecomm.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;
    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;
    /**Getting JWT from header
     String getJWTFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }**/

    //Getting Jwt from cookie
    public String getJwtFromCookie(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie != null){
            return cookie.getValue();
        }else{
            return null;
        }
    }

    //Generating jwtCookie from username
    public ResponseCookie generateJwtCookie(UserDetails userDetails){
        String jwt = generateTokenFromUsername(userDetails.getUsername());
        ResponseCookie responseCookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/api")//limits cookie access to /api routes only
                .maxAge(300)
                .httpOnly(false) //allowing javascript access to the coookie
                .build();
        return responseCookie;
    }
    // Generating token from username
    public String generateTokenFromUsername(String username){
        //String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }
    // Getting username for JWT token
    public String getUsernameFromJWTToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }
    // Generate signing key
    public Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
    // Validate JWT token
    public boolean validateJwtToken (String authToken){
        try{
            System.out.println("Validate");
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }catch(MalformedJwtException exception){
            logger.error("Invalid JWT token: {}", exception.getMessage());
        }catch(ExpiredJwtException exception){
            logger.error("JWT token is expired: {}", exception.getMessage());
        }catch(UnsupportedJwtException exception){
            logger.error("JWT token is unsupported: {}", exception.getMessage());
        }catch(IllegalArgumentException exception){
            logger.error("JWT claims string is empty: {}", exception.getMessage());
        }
        return false;
    }
    //Utility method for sign out. The null cookie overrrides the previous cookie and logs out teh user
    public ResponseCookie generateCleanJwtCookie(){
        ResponseCookie responseCookie = ResponseCookie.from(jwtCookie, null)
                .path("/api")//limits cookie access to /api routes only
                .build();
        return responseCookie;
    }
}
