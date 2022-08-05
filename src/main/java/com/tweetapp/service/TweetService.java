package com.tweetapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.tweetapp.constants.Constants;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Response;
import com.tweetapp.model.Tweets;
import com.tweetapp.repository.TweetRepository;

@Service
public class TweetService {
	
	@Autowired
	private TweetRepository tweetRepository;
	
	private Logger logger = LogManager.getLogger(TweetService.class);

	public ResponseEntity<Response> getAllTweets() {
		logger.info("Inside getAllTweets() ...");
		Response response;
		try {
			List<Tweets> list = tweetRepository.findAll();
			if(!ObjectUtils.isEmpty(list)) {
				response = new Response(Constants.SUCCESS, Constants.HTTP_OK, null, list);
			} else {
				response = new Response(Constants.FAILED, Constants.INTERNAL_ERROR, "No tweets Found", null);
			}
		} catch(Exception e) {
			logger.error("Error : ",e);
			response = new Response(Constants.FAILED, Constants.INTERNAL_ERROR, "No tweets Found", null);
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	public Response getTweetsByUsername(String username) {
		logger.info("Inside getTweetsByUsername() ...");
		Response response;
		try {
			Optional<List<Tweets>> optional = tweetRepository.findByUsername(username);
			if(optional.isPresent() && optional.get().size()>0) {
				response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "Tweets Found", optional.get());
			} else {
				response = new Response(Constants.FAILED, Constants.INTERNAL_ERROR, "No Tweets Found", null);
			}
		} catch(Exception e) {
			logger.error("Error : ",e);
			response = new Response(Constants.FAILED, Constants.INTERNAL_ERROR, "No Tweets Found", null);
		}
		return response;
	}

	public Response postNewTweet(String username, Tweets tweet) {
		logger.info("Inside postNewTweet() ...");
		Response response;
		tweet.setUsername(username);
		tweet.setCreationTime(LocalDateTime.now());
		tweet.setLikeCount(0);
		tweet.setLikedByList(new ArrayList<>());
		tweet.setReplies(new ArrayList<>());
		try {
			Tweets savedTweet = tweetRepository.save(tweet);
			response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "Tweet Saved");
		} catch(Exception e) {
			logger.error("Error : ",e);
			return new Response(Constants.FAILED, Constants.INTERNAL_ERROR,"Unable to save tweet");
		}
		return response;
	}

	public Response updateTweet(String username, String id, Tweets tweet) {
		logger.info("Inside updateTweet() ...");
		Response response;
		Optional<Tweets> optional = tweetRepository.findById(id);
		try {
			if(optional.isPresent()) {
				Tweets tweets = optional.get();
				tweets.setTweetDescription(tweet.getTweetDescription());
				tweets.setCreationTime(LocalDateTime.now());
				tweetRepository.save(tweets);
				response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "Tweet Updated");
			} else {
				response = new Response(Constants.FAILED, Constants.BAD_REQUEST, "Tweet Updation Failed");
			}
		} catch(Exception e) {
			logger.error("Error : ",e);
			response = new Response(Constants.FAILED, Constants.BAD_REQUEST, "Tweet Updation Failed");
		}
		return response;
	}

	public Response deleteTweet(String username, String id) {
		logger.info("Inside deleteTweet() ... ");
		Response response;
		try {
			tweetRepository.deleteById(id);
			logger.info("Tweet Deleted !");
			response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "Tweet Deleted");
		} catch (Exception e) {
			logger.error("Error : ",e);
			response = new Response(Constants.FAILED, Constants.BAD_REQUEST, "Tweet Deletion Failed");
		}
		return response;
	}

	public Response likeTweet(String username, String id) {
		logger.info("Inside likeTweet() ...");
		Response response;
		Optional<Tweets> optional = tweetRepository.findById(id);
		if(optional.isPresent()) {
			Tweets tweet = optional.get();
			List<String> likedByList = tweet.getLikedByList();
			if(!checkUsernameInList(username, likedByList)) {
				tweet.setLikeCount(tweet.getLikeCount()+1);
				likedByList.add(username);
				tweet.setLikedByList(likedByList);
				tweetRepository.save(tweet);
				response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "Tweet Liked");
			} else {
				response = new Response(Constants.FAILED, Constants.INTERNAL_ERROR, "User Already liked the tweet");
			}
			
		} else {
			response = new Response(Constants.FAILED, Constants.INTERNAL_ERROR, "No tweet with this id exists");
		}
		return response;
	}

	private boolean checkUsernameInList(String username, List<String> likedByList) {
		Optional<String> findAny = likedByList.stream().filter(rec -> rec.equals(username)).findAny();
		if(findAny.isPresent()) {
			return true;
		}
		return false;
	}

	public Response replyToTweet(String username, String id, Reply reply) {
		logger.info("Inside replyToTweet() ...");
		Response response;
		reply.setCreationTime(LocalDateTime.now());
		reply.setUsername(username);
		try {
			Optional<Tweets> optional = tweetRepository.findById(id);
			if(optional.isPresent()) {
				Tweets tweet = optional.get();
				List<Reply> replies = tweet.getReplies();
				replies.add(reply);
				tweet.setReplies(replies);
				Tweets savedTweet = tweetRepository.save(tweet);
				response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "Reply Added");
			} else {
				response = new Response(Constants.FAILED, Constants.BAD_REQUEST, "Unable to reply");
			}
		} catch(Exception e) {
			logger.error("Error : ",e);
			response = new Response(Constants.FAILED, Constants.BAD_REQUEST, "Unable to reply");
		}
		return response;
	}

}
