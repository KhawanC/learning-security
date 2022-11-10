package com.kaua.learning.security.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaua.learning.security.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/hello")
public class HelloResource {

	@SecurityRequirement(name = "bearerAuth")
	@GetMapping()
	@Operation(summary = "Just return a hello world string",
	description = "Just return a hello world string. Must have authorization",
	responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = {
					@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)
					)
			})})
	public String hello() {
		return "Hello World!";
	}
}
