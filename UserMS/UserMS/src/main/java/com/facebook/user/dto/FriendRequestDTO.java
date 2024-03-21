package com.facebook.user.dto;

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
    
}
