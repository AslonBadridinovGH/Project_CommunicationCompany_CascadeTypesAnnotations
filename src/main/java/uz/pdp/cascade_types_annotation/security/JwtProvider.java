package uz.pdp.cascade_types_annotation.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.cascade_types_annotation.entity.enumClass.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

   private static final long expireTime = 1000 * 60 * 60 * 24;   // 1 KUN
   private static final String secretSoz = "maxfiysozbunihechkimbilmasin";


   public String generateToken(String username, Set<Role> roles){
      Date expireDate = new Date(System.currentTimeMillis() + expireTime);
      String token = Jwts
              .builder()
              .setSubject(username)
              .setIssuedAt(new Date())
              .setExpiration(expireDate)
              .signWith(SignatureAlgorithm.HS512, secretSoz)
              .claim("roles", roles)
              .compact();
      return  token;
   }

   public String getEmailFromToken(String token){

        try {
               String email = Jwts
                       .parser()
                       .setSigningKey(secretSoz)
                       .parseClaimsJws(token)
                       .getBody()
                       .getSubject();
               return email;
        }catch (Exception e){
           return null;
        }
   }


}
