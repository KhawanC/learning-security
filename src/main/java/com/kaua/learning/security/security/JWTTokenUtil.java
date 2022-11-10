package com.kaua.learning.security.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kaua.learning.security.exception.AuthenticationException;
import com.kaua.learning.security.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTTokenUtil {

	@Value("${app.token_expiration}")
	private Long EXPIRATION_TIME;
	
	@Value("${app.encryption_key}")
	private String SECRET_KEY;
	
	public String generateToken(User user) {
		return Jwts.builder()
				.setSubject(user.getId() + "," + user.getEmail() + ",")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				.compact();
	} 
	
	public boolean validateTokenAccess(String token) throws AuthenticationException {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			throw new AuthenticationException("Token expired");
		} catch (IllegalArgumentException e) {
			throw new AuthenticationException("Token is null or is invalid");
		} catch (MalformedJwtException e) {
			throw new AuthenticationException("Token is invalid");
		} catch (UnsupportedJwtException e) {
			throw new AuthenticationException("Token not supported");
		} catch (SignatureException e) {
			throw new AuthenticationException("Token signature failed");
		}
	}
	
	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}
	
	private Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}
}
