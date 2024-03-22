package com.facebook.request.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.facebook.request.entity.FriendRequest;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, Long> {
	Optional<FriendRequest[]> findAllBySentToOrderBySentFrom(String sentBy);

	Optional<FriendRequest[]> findAllBySentFromOrderBySentTo(String sentFrom);

	Optional<FriendRequest> findBySentFromAndSentTo(String sentFrom, String sentTo);

}