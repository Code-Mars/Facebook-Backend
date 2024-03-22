package com.facebook.request.controller;

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
import org.springframework.web.reactive.function.client.WebClient;

import com.facebook.request.dto.FriendRequestDTO;
import com.facebook.request.dto.HttpMessageDTO;
import com.facebook.request.exception.FacebookException;
import com.facebook.request.service.FriendRequestService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping(value = "/requests")
@Validated
public class FriendRequestAPI {
    

	@Autowired
	private Environment environment;
	
	@Autowired
	private FriendRequestService friendRequestService;
	
	@PostMapping(value = "/exists")
	public ResponseEntity<HttpMessageDTO> isExistingFriendRequest(@Valid @RequestBody FriendRequestDTO friendRequestDTO) throws FacebookException {
		String exists = friendRequestService.isExistingFriendRequest(friendRequestDTO).toString();
		return new ResponseEntity<>(new HttpMessageDTO(exists), HttpStatus.OK);
	}
@PostMapping(value = "/send")
	public ResponseEntity<HttpMessageDTO> sendFriendRequest(@Valid @RequestBody FriendRequestDTO friendRequestDTO) throws FacebookException {
		friendRequestService.sendFriendRequest(friendRequestDTO);
		String message = environment.getProperty("FriendRequestAPI.REQUEST_SENT_SUCCESSFULLY");
		return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
	}
	@PostMapping(value = "/revoke/{id}")
	public ResponseEntity<HttpMessageDTO> revokeFriendRequest(@PathVariable Long id) throws FacebookException {
		friendRequestService.revokeFriendRequest(id);
		String message = environment.getProperty("FriendRequestAPI.REQUEST_REVOKED_SUCCESSFULLY");
		return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
	}
	@PostMapping(value = "/accept")
	public ResponseEntity<HttpMessageDTO> acceptFriendRequest(@Valid @RequestBody FriendRequestDTO friendRequestDTO) throws FacebookException {
		Long sentFromId = Long.valueOf(friendRequestDTO.getSentFrom());
		Long sentToId = Long.valueOf(friendRequestDTO.getSentTo());
		
		friendRequestService.acceptFriendRequest(sentFromId, sentToId, friendRequestDTO);
		String message = environment.getProperty("FriendRequestAPI.REQUEST_ACCEPT_SUCCESSFULLY");
		return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
	}
@PostMapping(value = "/decline/{id}")
	public ResponseEntity<HttpMessageDTO> declineFriendRequest(@PathVariable Long id) throws FacebookException {
		friendRequestService.declineFriendRequest(id);
		String message = environment.getProperty("FriendRequestAPI.REQUEST_DECLINE_SUCCESSFULLY");
		return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
	}
	@PostMapping(value = "/sentTo/{sentTo}")
	public ResponseEntity<FriendRequestDTO[]> getFriendRequestSentTo(@NotNull(message = "{sentto.notpresent}") @PathVariable String sentTo) throws FacebookException {
		FriendRequestDTO[] friendRequestDTOs = friendRequestService.getFriendRequestSentTo(sentTo);
		return new ResponseEntity<>(friendRequestDTOs, HttpStatus.OK);
	}
	
	@PostMapping(value = "/sentFrom/{sentFrom}")
	public ResponseEntity<FriendRequestDTO[]> getFriendRequestSentFrom(@NotNull(message = "{sentfrom.notpresent}") @PathVariable String sentFrom) throws FacebookException {
		FriendRequestDTO[] friendRequestDTOs = friendRequestService.getFriendRequestSentFrom(sentFrom);
		return new ResponseEntity<>(friendRequestDTOs, HttpStatus.OK);
	}private Long getUserId(String sentTo) throws FacebookException {
		String url = "http://localhost:9100/users/get/userid";
		Long id=WebClient.create().post().uri(url).bodyValue(sentTo).retrieve().bodyToMono(Long.class).block();
		if (id == null) throw new FacebookException("FriendRequestAPI.USER_ID_NOT_FOUND");
		return id;
	}
}
