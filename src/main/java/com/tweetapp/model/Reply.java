package com.tweetapp.model;

import java.time.LocalDateTime;

public class Reply {
	private String reply;
	private String tag;
	private String username;
	private LocalDateTime creationTime;

	public Reply() {
		super();
	}

	public Reply(String reply, String tag, String username, LocalDateTime creationTime) {
		super();
		this.reply = reply;
		this.tag = tag;
		this.username = username;
		this.creationTime = creationTime;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public String toString() {
		return "Reply [reply=" + reply + ", tag=" + tag + ", username=" + username + ", creationTime=" + creationTime
				+ "]";
	}

}
