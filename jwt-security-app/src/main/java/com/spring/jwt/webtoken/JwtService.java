package com.spring.jwt.webtoken;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET = "C62E213C82B951EFD55F907A3E3BF65EF50ECB7007781C8AED3A428D0B95755BCF2946243D617FD95098B0A9734BAF05849417088D63E4AAABA307549DCB4DB6";
	private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);
	
	public String generateToken(UserDetails userDetails) {
		Map<String, String> claims = new HashMap<>();
			claims.put("iss", "https://localhost:8080");
			
		return Jwts.builder()
				.claims(claims)
				.subject(userDetails.getUsername())
				.issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
				.signWith(generateKey())
				.compact();
	}
	
	private SecretKey generateKey() {
		byte[] decodedKey = Base64.getDecoder().decode(SECRET);
		return Keys.hmacShaKeyFor(decodedKey);
	}
	
	public String extractUserName(String jwt) {
		Claims claim = getClaims(jwt);	
		return claim.getSubject();
	}

	private Claims getClaims(String jwt) {
		Claims claim = Jwts.parser()
			.verifyWith(generateKey())
			.build()
			.parseSignedClaims(jwt)
			.getPayload();
		return claim;
	}
	public boolean isTokenValid(String jwt) {
		Claims claim = getClaims(jwt);
		return claim.getExpiration().after(Date.from(Instant.now()));
	}
}
