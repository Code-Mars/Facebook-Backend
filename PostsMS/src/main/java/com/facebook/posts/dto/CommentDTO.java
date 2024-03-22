package com.facebook.posts.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long commentedBy;
    
    private String commentText;
    private LocalDateTime timestamp;
    public Long getCommentedBy() {
        return commentedBy;
    }
    public void setCommentedBy(Long commentedBy) {
        this.commentedBy = commentedBy;
    }
    public String getCommentText() {
        return commentText;
    }
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((commentedBy == null) ? 0 : commentedBy.hashCode());
        result = prime * result + ((commentText == null) ? 0 : commentText.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
        CommentDTO other = (CommentDTO) obj;
        if (commentedBy == null) {
            if (other.commentedBy != null)
                return false;
        } else if (!commentedBy.equals(other.commentedBy))
            return false;
        if (commentText == null) {
            if (other.commentText != null)
                return false;
        } else if (!commentText.equals(other.commentText))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        return true;
    }
    
}
