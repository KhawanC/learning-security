package com.kaua.learning.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kaua.learning.security.converter.UserConverter;
import com.kaua.learning.security.dto.UserDTO;
import com.kaua.learning.security.exception.UserException;
import com.kaua.learning.security.form.UserForm;
import com.kaua.learning.security.model.User;
import com.kaua.learning.security.repository.UserRepository;
import com.kaua.learning.security.security.AuthRequest;
import com.kaua.learning.security.security.AuthResponse;
import com.kaua.learning.security.security.JWTTokenUtil;

@Service
public class UserService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	UserConverter converter;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	JWTTokenUtil tokenUtil;
	
	public List<UserDTO> findAll() throws UserException {
		List<User> listUser = repository.findAll();
		if(listUser.isEmpty())
			throw new UserException("The list of users is empty");
		
		List<UserDTO> listDTO = new ArrayList<>();
		for(User user : listUser) {
			listDTO.add(converter.ModelToDTO(user));
		}
		return listDTO;
	}
	
	public UserDTO findById(Integer id) throws UserException{
		Optional<User> user = repository.findById(id);
		if(user.isEmpty())
			throw new UserException("There is no user with the id " + id);
		else
			return converter.ModelToDTO(user.get());
	}
	
	public UserDTO findByEmail(String email) throws UserException{
		Optional<User> user = repository.findByEmail(email);
		if(user.isEmpty())
			throw new UserException("There is no user with the email " + email);
		else
			return converter.ModelToDTO(user.get());
	}
	
	public UserDTO saveUser(UserForm form) throws UserException {
			
		if(repository.findByEmail(form.getEmail()).isPresent())
			throw new UserException("There is already a user with the email " + form.getEmail());
		
		if(form.getPassword().length() > 10)
			throw new UserException("Password too long");
		
		User user = converter.FormToModel(form);
		
		user.setPassword(encoder.encode(user.getPassword()));
		
		return converter.ModelToDTO(repository.save(user));
	}
	
	public UserDTO updateUser(User user) throws UserException {
		Optional<User> oldUser = repository.findById(user.getId());
		if(oldUser.isEmpty())
			throw new UserException("There is no user with the id " + user.getId());
		
		if(oldUser.get() == user)
			throw new UserException("There are no changes in the user");
		
		if(user.getPassword() != oldUser.get().getPassword())
			if(user.getPassword().length() > 10)
				throw new UserException("Password too long");
			else
				user.setPassword(encoder.encode(user.getPassword()));
		
		return converter.ModelToDTO(repository.save(user));
	}
	
	public UserDTO deleteUserById(Integer id) throws UserException {
		Optional<User> oldUser = repository.findById(id);
		if(oldUser.isEmpty())
			throw new UserException("There is no user with the id " + id);
		
		repository.deleteById(id);
		
		return converter.ModelToDTO(oldUser.get());
	}
	
	public UserDTO deleteUserByEmail(String email) throws UserException {
		Optional<User> oldUser = repository.findByEmail(email);
		if(oldUser.isEmpty())
			throw new UserException("There is no user with the email " + email);
		repository.deleteById(oldUser.get().getId());
		
		return converter.ModelToDTO(oldUser.get());
	}
	
	public AuthResponse userAuthentication(AuthRequest request) throws Exception {
		try {
			Optional<User> user = repository.findByEmail(request.getEmail());
			if(user.isEmpty())
				throw new UserException("There is no user with the email " + request.getEmail());
			
			Authentication auth = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			User newUser = (User) auth.getPrincipal();
			String accessToken = tokenUtil.generateToken(newUser);
			AuthResponse res = new AuthResponse(newUser.getEmail(), accessToken);
			return res;
		} catch (BadCredentialsException e) {
			throw new UserException("Bad credential");
		} catch (IndexOutOfBoundsException e) {
			throw new UserException("Bad credential");
		}
	}
}
