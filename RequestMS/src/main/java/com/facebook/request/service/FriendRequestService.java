package com.facebook.request.service;

import com.facebook.request.dto.FriendRequestDTO;
import com.facebook.request.exception.FacebookException;

public interface FriendRequestService {
    public long getNextSequenceId(String key);
	FriendRequestDTO[] getFriendRequestSentTo(String sentTo) throws FacebookException;
	FriendRequestDTO[] getFriendRequestSentFrom(String sentFrom) throws FacebookException;
	void sendFriendRequest(FriendRequestDTO friendRequestDTO) throws FacebookException;
	public void revokeFriendRequest(Long id) throws FacebookException;
	void acceptFriendRequest(Long sentFromId, Long sentToId, FriendRequestDTO friendRequestDTO) throws FacebookException;
	void declineFriendRequest(Long id) throws FacebookException;
	Boolean isExistingFriendRequest(FriendRequestDTO friendRequestDTO) throws FacebookException;
}
