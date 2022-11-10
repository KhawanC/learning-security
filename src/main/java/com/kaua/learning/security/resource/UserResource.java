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
import com.kaua.learning.security.exception.ErrorResponse;
import com.kaua.learning.security.exception.UserException;
import com.kaua.learning.security.form.UserForm;
import com.kaua.learning.security.model.User;
import com.kaua.learning.security.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/user")
public class UserResource {
	
	// TODO add token request in swagger resource

	@Autowired
	UserService service;

	@GetMapping()
	@Operation(summary = "Get all users",
	description = "Get all users",
	responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = {
					@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)
					)
			})})
	public ResponseEntity<List<UserDTO>> findAllUser() throws UserException {
		return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("/byId/{id}")
	@Operation(summary = "Get users by id",
	description = "Get users by id",
	responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = {
					@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)
					)
			})})
	public ResponseEntity<UserDTO> findUserById(@PathVariable Integer id) throws UserException {
		return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
	}

	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/byEmail/{email}")
	@Operation(summary = "Get users by email.",
	description = "Get users by email. Must have authorization",
	responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = {
					@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)
					)
			})})
	public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) throws UserException {
		return new ResponseEntity<>(service.findByEmail(email), HttpStatus.OK);
	}

	@PostMapping()
	@Operation(summary = "Create a new user",
	description = "Create a new user. Password will be encrypted",
	responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = {
					@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)
					)
			})})
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserForm form) throws UserException {
		return new ResponseEntity<>(service.saveUser(form), HttpStatus.OK);
	}

	@SecurityRequirement(name = "bearerAuth")
	@PutMapping()
	@Operation(summary = "Update user information",
	description = "Update user information. If you don't wanna change the password just let the bcrpt hash equals as it was, otherwise you can change it and the pasasword will be encrypted again.",
	responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = {
					@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)
					)
			})})
	public ResponseEntity<UserDTO> updateUser(@RequestBody User user) throws UserException {
		return new ResponseEntity<>(service.updateUser(user), HttpStatus.OK);
	}

	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/byId/{id}")
	@Operation(summary = "Delete an user by id",
	description = "Delete an user by id. Must have authorization",
	responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = {
					@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)
					)
			})})
	public ResponseEntity<UserDTO> deleteUserById(@PathVariable Integer id) throws UserException {
		return new ResponseEntity<>(service.deleteUserById(id), HttpStatus.OK);
	}

	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/byEmail/{email}")
	@Operation(summary = "Delete an user by email",
	description = "Delete an user by email. Must have authorization",
	responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = {
					@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)
					)
			})})
	public ResponseEntity<UserDTO> deleteUserById(@PathVariable String email) throws UserException {
		return new ResponseEntity<>(service.deleteUserByEmail(email), HttpStatus.OK);
	}
}
