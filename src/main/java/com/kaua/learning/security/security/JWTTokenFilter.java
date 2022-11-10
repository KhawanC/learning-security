package com.kaua.learning.security.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kaua.learning.security.exception.AuthenticationException;
import com.kaua.learning.security.model.User;

@Component
public class JWTTokenFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		if (!hasAuthorizationHeader(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		String acessToken = getAccessToken(request);

		try {
			if (!jwtTokenUtil.validateTokenAccess(acessToken)) {
				filterChain.doFilter(request, response);
				return;
			}
		} catch (AuthenticationException | IOException | ServletException e) {
			e.printStackTrace();
		}

		setAuthenticationContext(acessToken, request);
		filterChain.doFilter(request, response);
	}

	private void setAuthenticationContext(String acessToken, HttpServletRequest request) {
		UserDetails userDetails = getUserDetails(acessToken);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
				null, null);

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private UserDetails getUserDetails(String acessToken) {
		User userDetails = new User();
		String[] subjectArray = jwtTokenUtil.getSubject(acessToken).split(",");
		userDetails.setId(Integer.parseInt(subjectArray[0]));
		userDetails.setEmail(subjectArray[1]);

		return userDetails;
	}

	private boolean hasAuthorizationHeader(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			return false;
		}
		return true;
	}

	private String getAccessToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String token = header.split(" ")[1].trim();
		return token;
	}

}
