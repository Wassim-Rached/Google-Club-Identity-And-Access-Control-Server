package com.example.usermanagement.filters;

import com.example.usermanagement.entities.Account;
import com.example.usermanagement.repositories.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String publicKey = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzTZAkC27UAR/FNX1kaAJ
            9IemjpYX1OILGWnjCxdMt1atFeCvTiaEZnHPI13p0r7YLw2DdQDePQEDpIrG1VbW
            adLUt6+4lHFFWIkllgnji5wJu/Og3cOXpzkezkBJdXSnCCBWPpnyLkMbUlGDayVL
            PflPsMs/YIL9alNs4jn5bENcLGmwtFkU3de27CrPR1QxHSPgptqYjaIJixwpp4mB
            c8mxl70/hedqSBW3FdjVRsLHCCr/v73s7VyCNYuMBa6m71PgX+sQtmK7hpG9nG4H
            53QdDSKoYERfmRW7dcbfYjokhxBJL9Q8KIzgMTy1QtEp3LM3L71xYMdToNcimsfX
            CQIDAQAB
            -----END PUBLIC KEY-----""";

    private final AccountRepository userAccountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                // Validate the JWT
                Claims claims = validateToken(jwtToken);
                // You can extract user details from the claims if needed
                String username = claims.getSubject(); // it's actually the user id
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Set the authentication in the context if it's not already set

                    UUID userId = UUID.fromString(username);
                    Account userAccount = userAccountRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Account to authenticate not found"));

                    Hibernate.initialize(userAccount.getAuthorities());

                    System.out.println("Account authorities: " + userAccount.getAuthorities());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userAccount, null, userAccount.getAuthorities());


                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Save the authentication to the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }


            } catch (Exception e) {
                System.out.println("JWT Token validation failed: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

    private Claims validateToken(String token) throws Exception {
        PublicKey key = getPublicKey();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private PublicKey getPublicKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzTZAkC27UAR/FNX1kaAJ9IemjpYX1OILGWnjCxdMt1atFeCvTiaEZnHPI13p0r7YLw2DdQDePQEDpIrG1VbWadLUt6+4lHFFWIkllgnji5wJu/Og3cOXpzkezkBJdXSnCCBWPpnyLkMbUlGDayVLPflPsMs/YIL9alNs4jn5bENcLGmwtFkU3de27CrPR1QxHSPgptqYjaIJixwpp4mBc8mxl70/hedqSBW3FdjVRsLHCCr/v73s7VyCNYuMBa6m71PgX+sQtmK7hpG9nG4H53QdDSKoYERfmRW7dcbfYjokhxBJL9Q8KIzgMTy1QtEp3LM3L71xYMdToNcimsfXCQIDAQAB");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }
}
