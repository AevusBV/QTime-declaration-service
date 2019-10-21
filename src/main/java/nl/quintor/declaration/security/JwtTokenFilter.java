package nl.quintor.declaration.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter for verifying JWT tokens
 */

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenValidator tokenValidator;

    public JwtTokenFilter(JwtTokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }


    @Override
    public void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        parseJwtTokenFromRequest(servletRequest).flatMap(token -> tokenValidator.getAuthentication(token))
                .ifPresent(authentication -> {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Optional<String> parseJwtTokenFromRequest(final HttpServletRequest httpRequest) {

        try {
            var authorizationHeader = httpRequest.getHeader("Authorization");
            var token = authorizationHeader.substring(7);

            return Optional.of(token);
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

}
