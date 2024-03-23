package com.facebook.friend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.facebook.friend.dto.FriendListDTO;
import com.facebook.friend.dto.HttpMessageDTO;
import com.facebook.friend.exception.FacebookException;
import com.facebook.friend.service.FriendService;

import jakarta.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping(value = "/friends")
@Validated
public class FriendAPI {
    @Autowired
	private Environment environment;
	
	@Autowired
	private FriendService friendService;
	@PostMapping(value = "/count")
	public ResponseEntity<HttpMessageDTO> getFriendCount(@NotNull(message = "{userid.notpresent}") @RequestBody Long userId) throws FacebookException {
		Integer friendCount = friendService.getFriendCount(userId);
		return new ResponseEntity<>(new HttpMessageDTO(friendCount.toString()), HttpStatus.OK);
	}
@PostMapping(value = "/register")
	public ResponseEntity<HttpMessageDTO> registerFriendList(@NotNull(message = "{userid.notpresent}") @RequestBody Long userId) throws FacebookException {
		friendService.registerFriendList(userId);
		String message = environment.getProperty("FriendAPI.FRIEND_LIST_REGISTER_SUCCESS");
		return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
	}
@PostMapping(value = "/add/{friend}")
	public ResponseEntity<HttpMessageDTO> addFriend(@NotNull(message = "{userid.notpresent}") @RequestBody Long userId,@NotNull(message = "{friend.notpresent}") @PathVariable Long friend) throws FacebookException {
		friendService.addFriend(userId, friend);
		String message = environment.getProperty("FriendAPI.FRIEND_ADDED_SUCCESS");
		return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
	}
@PostMapping(value = "/get")
	public ResponseEntity<List<Long>> getFriendList(@NotNull(message = "{userid.notpresent}") @RequestBody Long userId) throws FacebookException {
		FriendListDTO friendListDTO = friendService.getFriendList(userId);
		return new ResponseEntity<>(friendListDTO.getFriends(), HttpStatus.OK);
	}
@PostMapping(value = "/existing/friend/{friend}")
	public ResponseEntity<HttpMessageDTO> isExistingFriend(@NotNull(message = "{userid.notpresent}") @RequestBody Long userId, 
			@NotNull(message = "{friend.notpresent}") @PathVariable Long friend) throws FacebookException {
		String message = friendService.isExistingFriend(userId, friend).toString();
		return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
	}
	@PostMapping(value = "/remove/{friend}")
	public ResponseEntity<HttpMessageDTO> removeFriend(@NotNull(message = "{userid.notpresent}") @RequestBody Long userId, 
			@NotNull(message = "{friend.notpresent}") @PathVariable Long friend) throws FacebookException {
		friendService.removeFriend(userId, friend);
		String message = environment.getProperty("FriendAPI.FRIEND_REMOVE_SUCCESS");
		return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
	}
}
