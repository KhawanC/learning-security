package com.kaua.learning.security.converter;

import org.springframework.stereotype.Component;

import com.kaua.learning.security.dto.UserDTO;
import com.kaua.learning.security.form.UserForm;
import com.kaua.learning.security.model.User;

@Component
public class UserConverter {

	public User FormToModel(UserForm form) {
		User model = new User();
		
		model.setEmail(form.getEmail());
		model.setPassword(form.getPassword());
		
		return model;
	}
	
	public UserDTO ModelToDTO(User model) {
		UserDTO dto = new UserDTO();
		dto.setEmail(model.getEmail());
		dto.setPassword(model.getPassword());
		
		return dto;
	}
}
