package com.facebook.friend.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.facebook.friend.dto.FriendListDTO;

@Document("friend_list")
public class FriendList {
	@Id
	private Long id;
	private Long userId;
	private List<Long> friends;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public List<Long> getFriends() {
        return friends;
    }
    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }
    public FriendListDTO toDTO() {
		FriendListDTO friendListDTO = new FriendListDTO();
		friendListDTO.setId(id);
		friendListDTO.setUserId(userId);
		friendListDTO.setFriends(friends);
		return friendListDTO;
	}
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((friends == null) ? 0 : friends.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FriendList other = (FriendList) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (friends == null) {
            if (other.friends != null)
                return false;
        } else if (!friends.equals(other.friends))
            return false;
        return true;
    }
    
}
