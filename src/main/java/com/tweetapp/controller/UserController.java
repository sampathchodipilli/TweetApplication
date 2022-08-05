package com.tweetapp.controller;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.constants.Constants;
import com.tweetapp.dto.ForgotPasswordDto;
import com.tweetapp.exception.UsernameAlreadyExistsException;
import com.tweetapp.model.AuthRequest;
import com.tweetapp.model.AuthResponse;
import com.tweetapp.model.Response;
import com.tweetapp.model.User;
import com.tweetapp.service.UserService;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins = "*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private Logger logger = LogManager.getLogger(UserController.class);
	
	@GetMapping("/home")
	public String home() {
		return "Inside TweetApp() Home method ...";
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) throws UsernameAlreadyExistsException {
		User registerUser = userService.registerUser(user);
		Response response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "UserCreated !",registerUser);
		if(registerUser == null) {
			response = new Response(Constants.FAILED, Constants.INTERNAL_ERROR, "Unable to create user", null);
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
		AuthResponse authResponse = userService.login(authRequest);
		logger.info("Token info = "+authResponse.toString());
		Response response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "User Logged in", authResponse);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
		
	}
	
	@GetMapping("/{username}/forgot")
	public ResponseEntity<?> forgrtPassword(@PathVariable("username") String username, @RequestBody ForgotPasswordDto forgotPasswordDto) {
		Response response = userService.forgotPassword(username, forgotPasswordDto);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/users/all")
	public ResponseEntity<?> getAllUsers() {
		Response allUsers = userService.getAllUsers();
		return new ResponseEntity<Response>(allUsers, HttpStatus.OK);
	}
	
	@GetMapping("/user/search/{username}")
	public ResponseEntity<?> searchByUsername(@PathVariable("username") String username) {
		Response response = userService.getUserByUsername(username);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
}
