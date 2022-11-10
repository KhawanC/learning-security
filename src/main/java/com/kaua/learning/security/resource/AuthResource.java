package com.kaua.learning.security.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaua.learning.security.security.AuthRequest;
import com.kaua.learning.security.security.AuthResponse;
import com.kaua.learning.security.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthResource {

	@Autowired
	UserService service;
	
	@PostMapping
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) throws Exception {
		return new ResponseEntity<>(service.userAuthentication(request), HttpStatus.ACCEPTED);
	}
}
