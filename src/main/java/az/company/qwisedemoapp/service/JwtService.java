package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.util.RSAKeyUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    //    @Value("${jwt.secret}")
    //    private String secretKey;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtService() throws Exception {
        this.privateKey = RSAKeyUtil.getPrivateKey("src/main/resources/private.key");
        this.publicKey = RSAKeyUtil.getPublicKey("src/main/resources/public.key");
    }

    @Value("${jwt.expirationMs}")
    private String expirationMs;

//    public SecretKey getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());
        return createToken(user, claims);
    }

    private String createToken(User user, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationMs)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }


    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
