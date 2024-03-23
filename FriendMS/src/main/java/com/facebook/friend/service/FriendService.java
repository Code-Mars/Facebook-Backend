package com.facebook.friend.service;

import com.facebook.friend.dto.FriendListDTO;
import com.facebook.friend.exception.FacebookException;

public interface FriendService {
    public long getNextSequenceId(String key);
	FriendListDTO getFriendList(Long userId) throws FacebookException;
	void addFriend(Long userId, Long friend) throws FacebookException;
	void removeFriend(Long userId, Long friend) throws FacebookException;
	void registerFriendList(Long userId) throws FacebookException;
	Integer getFriendCount(Long userId) throws FacebookException;
	Boolean isExistingFriend(Long userId, Long friend) throws FacebookException;
}
