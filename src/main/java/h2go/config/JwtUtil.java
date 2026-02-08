package h2go.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtil {

  private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpiration;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    log.info("JWT secret key initialized successfully");
  }

  public String generateToken(String username) {
    String token = Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + jwtExpiration))
        .signWith(secretKey)
        .compact();

    log.info("JWT token generated successfully for user: {}", username);
    return token;
  }

  public String getUserFromToken(String token) {
    String username = Jwts.parser().verifyWith(secretKey).build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
    log.debug("Extracted username from token: {}", username);
    return username;
  }

  public Boolean validateJwtToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      log.debug("JWT token validation successful");
      return true;
    } catch (Exception e) {
      log.error("JWT Validation Error: {}", e.getMessage());
      log.error("Validation exception details:", e);
    }
    return false;
  }
}
