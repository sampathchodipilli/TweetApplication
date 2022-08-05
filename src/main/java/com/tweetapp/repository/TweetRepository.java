package com.tweetapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.Tweets;

public interface TweetRepository extends MongoRepository<Tweets, String> {
	Optional<List<Tweets>> findByUsername(String username);
}