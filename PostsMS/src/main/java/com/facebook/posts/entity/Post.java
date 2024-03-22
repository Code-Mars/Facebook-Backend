package com.facebook.posts.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.facebook.posts.dto.CommentDTO;
import com.facebook.posts.dto.PostDTO;
import com.facebook.posts.dto.Privacy;

@Document(value = "post")
public class Post {

    @Id
    private Long id;
    private Long userId;
    private String postedBy;
    private String postText;
    private String postImage;
    private String postVideo;
    private LocalDateTime timestamp;
    private Privacy privacy;
    private List<Long> likes;
    private List<CommentDTO> comments;
    private String sharedFrom;

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

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(String postVideo) {
        this.postVideo = postVideo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public List<Long> getLikes() {
        return likes;
    }

    public void setLikes(List<Long> likes) {
        this.likes = likes;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
    

    public PostDTO toDTO() {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(this.id);
        postDTO.setUserId(this.userId);
        postDTO.setPostedBy(this.postedBy);
        postDTO.setPostText(this.postText);
        postDTO.setPostImage(this.postImage);
        postDTO.setPostVideo(this.postVideo);
        postDTO.setTimestamp(this.timestamp);
        postDTO.setPrivacy(this.privacy);
        postDTO.setLikes(this.likes);
        postDTO.setComments(this.comments);
        postDTO.setSharedFrom(this.sharedFrom);
        return postDTO;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((postedBy == null) ? 0 : postedBy.hashCode());
        result = prime * result + ((postText == null) ? 0 : postText.hashCode());
        result = prime * result + ((postImage == null) ? 0 : postImage.hashCode());
        result = prime * result + ((postVideo == null) ? 0 : postVideo.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((privacy == null) ? 0 : privacy.hashCode());
        result = prime * result + ((likes == null) ? 0 : likes.hashCode());
        result = prime * result + ((comments == null) ? 0 : comments.hashCode());
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
        Post other = (Post) obj;
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
        if (postedBy == null) {
            if (other.postedBy != null)
                return false;
        } else if (!postedBy.equals(other.postedBy))
            return false;
        if (postText == null) {
            if (other.postText != null)
                return false;
        } else if (!postText.equals(other.postText))
            return false;
        if (postImage == null) {
            if (other.postImage != null)
                return false;
        } else if (!postImage.equals(other.postImage))
            return false;
        if (postVideo == null) {
            if (other.postVideo != null)
                return false;
        } else if (!postVideo.equals(other.postVideo))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (privacy == null) {
            if (other.privacy != null)
                return false;
        } else if (!privacy.equals(other.privacy))
            return false;
        if (likes == null) {
            if (other.likes != null)
                return false;
        } else if (!likes.equals(other.likes))
            return false;
        if (comments == null) {
            if (other.comments != null)
                return false;
        } else if (!comments.equals(other.comments))
            return false;
        return true;
    }

    public String getSharedFrom() {
        return sharedFrom;
    }

    public void setSharedFrom(String sharedFrom) {
        this.sharedFrom = sharedFrom;
    }
}
