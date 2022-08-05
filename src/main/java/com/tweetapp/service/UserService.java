package com.tweetapp.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import com.tweetapp.constants.Constants;
import com.tweetapp.dto.ForgotPasswordDto;
import com.tweetapp.exception.UsernameAlreadyExistsException;
import com.tweetapp.model.AuthRequest;
import com.tweetapp.model.AuthResponse;
import com.tweetapp.model.Response;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenService jwtTokenService;
	
	private Logger logger = LogManager.getLogger(UserService.class);

	public AuthResponse login(AuthRequest authRequest) {
		logger.info("Inside login() ...");
		AuthResponse authResponse;
		Optional<User> optional = userRepository.findByEmailAndPassword(authRequest.getUsername(), authRequest.getPassword());
		if (optional.isPresent()) {
			String acccessToken = jwtTokenService.generateAcccessToken(authRequest.getUsername());
			authResponse = new AuthResponse(acccessToken, authRequest.getUsername());
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unable to login, Check your credentials !");
		}
		return authResponse;
	}

	public User registerUser(User user) throws UsernameAlreadyExistsException {
		logger.info("Inside registerUser() ...");
		User savedUser;
		Optional<User> findByEmail = userRepository.findByEmail(user.getEmail());
		if (findByEmail.isPresent()) {
			throw new UsernameAlreadyExistsException("The Entered Username is already taken !");
		} else {
			try {
				savedUser = userRepository.save(user);
			} catch(Exception e) {
				logger.error("Error : ",e);
				savedUser = null;
			}
		}
		return savedUser;
	}

	public Response forgotPassword(String username, ForgotPasswordDto forgotPasswordDto) {
		logger.info("Inside forgotPassword() ...");
		Response response;
		Optional<User> optional = userRepository.findByEmail(username);
		if(optional.isPresent()) {
			User user = optional.get();
			user.setPassword(forgotPasswordDto.getPassword());
			userRepository.save(user);
			logger.info("User password updated !");
			response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "Password changed successfully");
		} else {
			response = new Response(Constants.FAILED, Constants.BAD_REQUEST, "Unable to change password, please check the username");
		}
		return response;
	}

	public Response getAllUsers() {
		logger.info("Inside getAllUsers() ...");
		Response response;
		List<User> findAll = userRepository.findAll();
		if (!ObjectUtils.isEmpty(findAll)) {
			System.out.println("findall = "+findAll);
			response = new Response(Constants.SUCCESS, Constants.HTTP_OK, null, findAll);
		} else {
			response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "No users Found !", null);
		}
		return response;
	}

	public Response getUserByUsername(String username) {
		logger.info("Inside getUserByUsername() ...");
		Response response;
		Optional<User> optional = userRepository.findByEmail(username);
		if (optional.isPresent()) {
			response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "User Found", optional.get());
		} else {
			response = new Response(Constants.FAILED, Constants.BAD_REQUEST, "User Not Found", null);
		}
		return response;
	}

}
