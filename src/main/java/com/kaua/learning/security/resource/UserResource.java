package com.kaua.learning.security.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaua.learning.security.dto.UserDTO;
import com.kaua.learning.security.exception.UserException;
import com.kaua.learning.security.form.UserForm;
import com.kaua.learning.security.model.User;
import com.kaua.learning.security.service.UserService;

@RestController
@RequestMapping("/user")
public class UserResource {
	
	// TODO add token request in swagger resource

	@Autowired
	UserService service;

	@GetMapping()
	public ResponseEntity<List<UserDTO>> findAllUser() throws UserException {
		return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("/byId/{id}")
	public ResponseEntity<UserDTO> findUserById(@PathVariable Integer id) throws UserException {
		return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
	}

	@GetMapping("/byEmail/{email}")
	public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) throws UserException {
		return new ResponseEntity<>(service.findByEmail(email), HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserForm form) throws UserException {
		return new ResponseEntity<>(service.saveUser(form), HttpStatus.OK);
	}

	@PutMapping()
	public ResponseEntity<UserDTO> updateUser(@RequestBody User user) throws UserException {
		return new ResponseEntity<>(service.updateUser(user), HttpStatus.OK);
	}

	@DeleteMapping("/byId/{id}")
	public ResponseEntity<UserDTO> deleteUserById(@PathVariable Integer id) throws UserException {
		return new ResponseEntity<>(service.deleteUserById(id), HttpStatus.OK);
	}

	@DeleteMapping("/byEmail/{email}")
	public ResponseEntity<UserDTO> deleteUserById(@PathVariable String email) throws UserException {
		return new ResponseEntity<>(service.deleteUserByEmail(email), HttpStatus.OK);
	}
}
