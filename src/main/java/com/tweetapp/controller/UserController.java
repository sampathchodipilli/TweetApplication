package com.tweetapp.controller;


import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.constants.Constants;
import com.tweetapp.dto.ForgotPasswordDto;
import com.tweetapp.model.AuthRequest;
import com.tweetapp.model.AuthResponse;
import com.tweetapp.model.Response;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.service.JwtTokenService;
import com.tweetapp.service.UserService;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins = "*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	private Logger logger = LogManager.getLogger(UserController.class);
	
	@GetMapping("/home")
	public ResponseEntity<Response> home(@RequestHeader Map<String, String> headers) throws Exception {
		logger.info("Inside TweetApp() Home method ...");
//		User user = null;
//		headers.forEach((k,v) -> {
//			logger.info(k," - ",v);
//		});
//		String bearerToken = headers.get("Authorization");
//		String username = jwtTokenService.validateTokenAndGetUsername(bearerToken);
//		try {
//			Optional<User> findByEmail = userRepository.findByEmail(username);
//			if(findByEmail.isPresent()) {
//				user = findByEmail.get();
//			}
//		} catch(Exception e) {
//			throw new Exception("User Not Authenticated !");
//		}
		return new ResponseEntity<Response>(new Response(Constants.SUCCESS, Constants.HTTP_OK, "User Authenticated !"), HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<Response> registerUser(@RequestBody User user) throws Exception {
		User registerUser = userService.registerUser(user);
		Response response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "UserCreated !",registerUser);
		if(registerUser == null) {
			response = new Response(Constants.FAILED, Constants.INTERNAL_ERROR, "Unable to create user", null);
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody AuthRequest authRequest) {
		AuthResponse authResponse = userService.login(authRequest);
		logger.info("Token info = "+authResponse.toString());
		Response response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "User Logged in", authResponse);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
		
	}
	
	@PostMapping("/{username}/forgot")
	public ResponseEntity<Response> forgrtPassword(@PathVariable("username") String username, @RequestBody ForgotPasswordDto forgotPasswordDto) {
		Response response = userService.forgotPassword(username, forgotPasswordDto);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/users/all")
	public ResponseEntity<Response> getAllUsers() {
		Response allUsers = userService.getAllUsers();
		return new ResponseEntity<Response>(allUsers, HttpStatus.OK);
	}
	
	@GetMapping("/user/search/{username}")
	public ResponseEntity<Response> searchByUsername(@PathVariable("username") String username) {
		Response response = userService.getUserByUsername(username);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
}
