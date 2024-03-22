package com.facebook.posts.dto;

public class FriendRequestDTO {
    private Long id;
    private String sentTo;
    private String sentFrom;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSentTo() {
        return sentTo;
    }
    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }
    public String getSentFrom() {
        return sentFrom;
    }
    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((sentTo == null) ? 0 : sentTo.hashCode());
        result = prime * result + ((sentFrom == null) ? 0 : sentFrom.hashCode());
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
        FriendRequestDTO other = (FriendRequestDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (sentTo == null) {
            if (other.sentTo != null)
                return false;
        } else if (!sentTo.equals(other.sentTo))
            return false;
        if (sentFrom == null) {
            if (other.sentFrom != null)
                return false;
        } else if (!sentFrom.equals(other.sentFrom))
            return false;
        return true;
    }
    
}
