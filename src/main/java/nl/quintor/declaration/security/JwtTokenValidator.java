package nl.quintor.declaration.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;


public class JwtTokenValidator {
    private static final String ROLES_KEY = "roles";
    private static final String ROLES_DELIMITTER = ",";

    public Optional<Authentication> getAuthentication(String token) {
        Jwt<Header, Claims> claims;

        try {
            var endOfkeyWithoutSignature = token.lastIndexOf('.') + 1;
            claims = Jwts.parser()
                    .parseClaimsJwt(token.substring(0, endOfkeyWithoutSignature));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

        var username = claims.getBody().getSubject();
        var roles = Arrays.stream(claims.getBody()
                .get(ROLES_KEY, String.class)
                .split(ROLES_DELIMITTER))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return Optional.of(new UsernamePasswordAuthenticationToken(username, "", roles));
    }
}
