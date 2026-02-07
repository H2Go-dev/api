package h2go.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import h2go.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);
  
  public static final String BEARER_ = "Bearer ";
  private final JwtUtil jwtUtil;
  private final CustomUserDetailsService userDetailsService;

  public AuthTokenFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
        final String username = jwtUtil.getUserFromToken(jwt);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
            null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    } catch (Exception e) {
      log.error("Cannot set user authentication: {}", e.getMessage());
    }
    filterChain.doFilter(request, response);

  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (headerAuth != null && headerAuth.startsWith(BEARER_)) {
      return headerAuth.substring(BEARER_.length());
    }
    return null;
  }

}
