package com.tweetapp.model;

public class AuthResponse {

	private final String jwt;

	private final String username;

	public AuthResponse(String jwt, String username) {
		super();
		this.jwt = jwt;
		this.username = username;
	}

	public String getJwt() {
		return jwt;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public String toString() {
		return "AuthResponse [jwt=" + jwt + ", username=" + username + "]";
	}

}
