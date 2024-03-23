package com.facebook.friend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.facebook.friend.entity.FriendList;

public interface FriendRepository extends MongoRepository<FriendList, Long> {
	Optional<FriendList> findByUserId(Long userId);
}
