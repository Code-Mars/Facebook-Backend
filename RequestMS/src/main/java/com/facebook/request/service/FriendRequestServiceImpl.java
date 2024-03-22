package com.facebook.request.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.facebook.request.dto.FriendRequestDTO;
import com.facebook.request.dto.HttpMessageDTO;
import com.facebook.request.entity.FriendRequest;
import com.facebook.request.entity.SequenceId;
import com.facebook.request.exception.FacebookException;
import com.facebook.request.repository.FriendRequestRepository;

@Service(value = "friendRequestService")
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private MongoOperations mongoOperation;
    private static final String HOSTING_SEQ_KEY = "hosting";

    @Override
    public long getNextSequenceId(String key) {
        Query query = new Query(Criteria.where("_id").is(key));
        Update update = new Update();
        update.inc("seq", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        SequenceId seqId = mongoOperation.findAndModify(query, update, options, SequenceId.class);
        return seqId.getSeq();
    }

    @Override
    public FriendRequestDTO[] getFriendRequestSentTo(String sentTo) throws FacebookException {
        Optional<FriendRequest[]> friendRequestsOptional = friendRequestRepository
                .findAllBySentToOrderBySentFrom(sentTo);
        if (!friendRequestsOptional.isPresent())
            return new FriendRequestDTO[0];
        FriendRequest[] friendRequests = friendRequestsOptional.get();
        FriendRequestDTO[] friendRequestDTOs = new FriendRequestDTO[friendRequests.length];
        for (int i = 0; i < friendRequests.length; i++) {
            friendRequestDTOs[i] = friendRequests[i].toDTO();
        }

        return friendRequestDTOs;
    }

    @Override
    public FriendRequestDTO[] getFriendRequestSentFrom(String sentFrom) throws FacebookException {
        Optional<FriendRequest[]> friendRequestsOptional = friendRequestRepository
                .findAllBySentFromOrderBySentTo(sentFrom);
        if (!friendRequestsOptional.isPresent())
            return new FriendRequestDTO[0];
        FriendRequest[] friendRequests = friendRequestsOptional.get();
        FriendRequestDTO[] friendRequestDTOs = new FriendRequestDTO[friendRequests.length];
        for (int i = 0; i < friendRequests.length; i++) {
            friendRequestDTOs[i] = friendRequests[i].toDTO();
        }

        return friendRequestDTOs;
    }

    @Override
    public void sendFriendRequest(FriendRequestDTO friendRequestDTO) throws FacebookException {
        if (isExistingFriendRequest(friendRequestDTO).booleanValue())
            throw new FacebookException("Service.FRIEND_REQUEST_ALREADY_EXISTS");

        FriendRequest friendRequest = friendRequestDTO.toEntity();
        friendRequest.setId(getNextSequenceId(HOSTING_SEQ_KEY));
        friendRequestRepository.save(friendRequest);
    }

    @Override
    public void revokeFriendRequest(Long id) throws FacebookException {
        friendRequestRepository.deleteById(id);
    }

    @Override
    public void acceptFriendRequest(Long sentFromId, Long sentToId, FriendRequestDTO friendRequestDTO)
            throws FacebookException {
        String url = "http://localhost:9500/friends/add/" + sentToId;
        WebClient.create().post().uri(url).bodyValue(sentFromId).retrieve().bodyToMono(HttpMessageDTO.class).block();
        friendRequestRepository.deleteById(friendRequestDTO.getId());
    }

    @Override
    public void declineFriendRequest(Long id) throws FacebookException {
        friendRequestRepository.deleteById(id);
    }

    @Override
    public Boolean isExistingFriendRequest(FriendRequestDTO friendRequestDTO) throws FacebookException {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository
                .findBySentFromAndSentTo(friendRequestDTO.getSentFrom(), friendRequestDTO.getSentTo());
        return friendRequestOptional.isPresent();
    }
}
