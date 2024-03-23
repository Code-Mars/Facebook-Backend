package com.facebook.friend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facebook.friend.dto.FriendListDTO;
import com.facebook.friend.entity.FriendList;
import com.facebook.friend.entity.SequenceId;
import com.facebook.friend.exception.FacebookException;
import com.facebook.friend.repository.FriendRepository;

@Service(value = "friendListService")
@Transactional
public class FriendServiceImpl implements FriendService{
    
private static final String FRIEND_LIST_NOT_FOUND = "Service.FRIEND_LIST_NOT_FOUND";
	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private MongoOperations mongoOperations;
	private static final String HOSTING_SEQ_KEY = "hosting";
	@Override
	public long getNextSequenceId(String key){
		Query query = new Query(Criteria.where("_id").is(key));
		Update update = new Update();
		update.inc("seq", 1);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		SequenceId seqId = mongoOperations.findAndModify(query, update, options, SequenceId.class);
		return seqId.getSeq();
	}
@Override
	public Integer getFriendCount(Long userId) throws FacebookException {
		FriendList friendList = friendRepository.findByUserId(userId).orElseThrow(()-> new FacebookException(FRIEND_LIST_NOT_FOUND));
		return friendList.getFriends().size();
	}
	@Override
	public FriendListDTO getFriendList(Long userId) throws FacebookException {
		FriendList friendList = friendRepository.findByUserId(userId).orElseThrow(()-> new FacebookException(FRIEND_LIST_NOT_FOUND));
		return friendList.toDTO();
	}
@Override
	public Boolean isExistingFriend(Long userId, Long friend) throws FacebookException {
		FriendList friendList = friendRepository.findByUserId(userId).orElseThrow(()-> new FacebookException(FRIEND_LIST_NOT_FOUND));
		return friendList.getFriends().contains(friend);
	}
	@Override
	public void addFriend(Long userId, Long friend) throws FacebookException {
		this.add(userId, friend);
		this.add(friend, userId);
	}
public void add(Long userId, Long friend) throws FacebookException {
		FriendList friendList = friendRepository.findByUserId(userId).orElseThrow(()-> new FacebookException(FRIEND_LIST_NOT_FOUND));
		List<Long> friends = friendList.getFriends();
		friends.add(friend);
		friendList.setFriends(friends);
		friendRepository.save(friendList);
	}
	@Override
	public void removeFriend(Long userId, Long friend) throws FacebookException {
		this.remove(userId, friend);
		this.remove(friend, userId);
	}
public void remove(Long userId, Long friend)throws FacebookException{
		FriendList friendList = friendRepository.findByUserId(userId).orElseThrow(()-> new FacebookException(FRIEND_LIST_NOT_FOUND));
		List<Long> friends = friendList.getFriends();
		friends.removeIf(f -> f.equals(friend));
		friendRepository.save(friendList);
	}
	@Override
	public void registerFriendList(Long userId) throws FacebookException {
		FriendList friendList = new FriendList();
		friendList.setId(getNextSequenceId(HOSTING_SEQ_KEY));
		friendList.setUserId(userId);
		friendList.setFriends(new ArrayList<Long>());
		friendRepository.save(friendList);
	}
}
