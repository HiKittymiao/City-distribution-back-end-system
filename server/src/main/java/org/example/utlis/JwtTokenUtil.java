package org.example.utlis;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:JwtTokenUtil
 * Package:com.cbb.server.config.security
 * Description:
 *
 * @Date:2022/5/12 20:49
 * @Author:cbb
 */
@Component
public class JwtTokenUtil {
    private static  final  String CLAIM_KEY_USERNAME="sub";
    private static  final  String CLAIM_KEY_CREATED="created";
    @Value("${jwt.secret}")
    private  String secret;
    @Value("${jwt.expiration}")
    private  Long expiration ;



    //根据用户信息 生成 token
    public String generateToken(UserDetails userDetails){
        Map<String , Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());

        return  generateToken(claims);
    }
    //根据 荷载生成JWT  Token
    private String  generateToken(Map<String , Object> claims){
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512,secret).compact();
    }

    //生成token失效时间
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    //从 token 中获取用户名
    public String getUserNameFromToken(String token){
        String username;

        try{
            Claims claims = getClaimsFormToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            username=null;

        }
        return username;
    }

    //从 token 中获取荷载
    private Claims getClaimsFormToken(String token) {
        Claims claims = null;
        try {
             claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }

        return claims;
    }

    //判断token 是否有效
    public boolean validateToken(String token,UserDetails userDetails){
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && ! isTokenExpired(token);
    }
    //判断token是否失效

    private boolean isTokenExpired(String token) {
        Claims claims = getClaimsFormToken(token);
        Date date  = claims.getExpiration();
        return date.before(new Date());

    }

    //判断token 是否可以被刷新
    public  boolean canRefresh(String token){
        return  !isTokenExpired(token);
    }

    //刷新token
    public  String refreshToken(String token){
        Claims claims = getClaimsFormToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

}
