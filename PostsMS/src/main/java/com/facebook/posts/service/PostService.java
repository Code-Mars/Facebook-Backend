package com.facebook.posts.service;

import java.util.List;

import com.facebook.posts.dto.CommentDTO;
import com.facebook.posts.dto.LikeDTO;
import com.facebook.posts.dto.PostDTO;
import com.facebook.posts.exception.FacebookException;

public interface PostService {
    public long getNextSequenceId(String key);
    void createPost(PostDTO postDTO) throws FacebookException;
    void updatePost(PostDTO postDTO) throws FacebookException;
    List<PostDTO> getMyRecentPosts(Long id) throws FacebookException;
    public List<PostDTO> getRecentPosts(Long id, List<Long> friends, String s) throws FacebookException;
    List<PostDTO> getUserPosts(Long id, String s) throws FacebookException;
    List<PostDTO> getFriendPosts(Long id, Boolean isFriend, String s) throws FacebookException;
    Integer getPostCount(Long id) throws FacebookException;
    Integer getPublicOnlyPostCount(Long id) throws FacebookException;
    Integer getNonPrivatePostCount(Long id) throws FacebookException;
    public void likePost(LikeDTO likeDTO) throws FacebookException;
    public void addComment(Long id, CommentDTO commentDTO) throws FacebookException;
    void deletePost(Long id) throws FacebookException;
}
