package com.facebook.posts.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.facebook.posts.dto.CommentDTO;
import com.facebook.posts.dto.LikeDTO;
import com.facebook.posts.dto.PostDTO;
import com.facebook.posts.dto.Privacy;
import com.facebook.posts.entity.Post;
import com.facebook.posts.entity.SequenceId;
import com.facebook.posts.exception.FacebookException;
import com.facebook.posts.repository.PostRepository;

@Service("postService")
public class PostServiceImpl implements PostService {
    private static final String TIMESTAMP = "timestamp";
    private static final String POSTS_NOT_FOUND = "Service.POSTS_NOT_FOUND";
    @Autowired
    private PostRepository postRepository;
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
    public void createPost(PostDTO postDTO) throws FacebookException {
        postDTO.setTimestamp(LocalDateTime.now());
        postDTO.setId(getNextSequenceId(HOSTING_SEQ_KEY));
        postRepository.save(postDTO.toEntity());
    }
    @Override
    public void updatePost(PostDTO postDTO) throws FacebookException {
        Post post = postDTO.toEntity();
        postRepository.save(post);
    }
    @Override
    public void deletePost(Long id) throws FacebookException {
        postRepository.deleteById(id);
    }
    @Override
    public List<PostDTO> getMyRecentPosts(Long id) throws FacebookException {
        List<Post> recentPosts = postRepository.findTop4ByUserIdOrderByTimestampDesc(id);
        if(recentPosts.isEmpty())new FacebookException(POSTS_NOT_FOUND);
        List<PostDTO> recentPostDTOs = new ArrayList<>();
        recentPosts.forEach((x)->recentPostDTOs.add(x.toDTO()));
        return recentPostDTOs;
    }
    @Override
    public List<PostDTO> getRecentPosts(Long id, List<Long> friends, String s) throws FacebookException {
        Sort sort = Sort.by(TIMESTAMP).descending();
        List<Post> recentPosts = postRepository.findAllByUserIdOrPrivacyOrUserIdInAndPrivacy(id,Privacy.PUBLIC, friends,Privacy.FRIENDS, sort);
        List<PostDTO> recentPostDTOs = new ArrayList<>();
        recentPosts.forEach((x)->recentPostDTOs.add(x.toDTO()));
        recentPostDTOs.forEach((x)->{
            if(x.getLikes()==null)x.setLikes(new ArrayList<>());
            x.setHasLiked(x.getLikes().contains(id));
        });
        if(!s.equalsIgnoreCase("recent"))return sortObj(recentPostDTOs, s);
        return recentPostDTOs;
    }
    public List<PostDTO> sortObj(List<PostDTO> recentPostDTOs, String s){
        if(s.equalsIgnoreCase("earlier")) recentPostDTOs.sort(Comparator.comparing(obj -> obj.getTimestamp()));
        else {
            recentPostDTOs.sort(Comparator.comparingInt(obj -> obj.getLikes().size()));
        Collections.reverse(recentPostDTOs);
        }
        return recentPostDTOs;
    }
    @Override
    public List<PostDTO> getUserPosts(Long id, String s) throws FacebookException {
        Sort sort = Sort.by(TIMESTAMP).descending();
        List<Post> recentPosts  = postRepository.findAllByUserId(id, sort);
        List<PostDTO> recentPostDTOs = new ArrayList<>();
        recentPosts.forEach((x)->recentPostDTOs.add(x.toDTO()));
        recentPostDTOs.forEach((x)->{
            if(x.getLikes()==null)x.setLikes(new ArrayList<>());
            x.setHasLiked(x.getLikes().contains(id));
        });
        if(!s.equalsIgnoreCase("recent"))return sortObj(recentPostDTOs, s);
        return recentPostDTOs;
    }
    @Override
    public List<PostDTO> getFriendPosts(Long id, Boolean isFriend, String s ) throws FacebookException {
        Sort sort = Sort.by(TIMESTAMP).descending();
        
        ArrayList<Privacy> privacy = new ArrayList<>();
        privacy.add(Privacy.PUBLIC);
        if (isFriend.booleanValue()) privacy.add(Privacy.FRIENDS);
        List<Post> recentPosts = postRepository.findAllByUserIdAndPrivacyIn(id, privacy, sort);
        List<PostDTO> recentPostDTOs = new ArrayList<>();
        recentPosts.forEach((x)->recentPostDTOs.add(x.toDTO()));
        recentPostDTOs.forEach((x)->{
            if(x.getLikes()==null)x.setLikes(new ArrayList<>());
            x.setHasLiked(x.getLikes().contains(id));
        });
        if(!s.equalsIgnoreCase("Recent"))return sortObj(recentPostDTOs, s);
        return recentPostDTOs;
    }
    @Override
    public void likePost(LikeDTO likeDTO) throws FacebookException {
        Post p=postRepository.findById(likeDTO.getPostId()).orElseThrow(()->new FacebookException("Post not Found"));
        if(p.getLikes()==null)p.setLikes(new ArrayList<Long>());
        List<Long>likes=p.getLikes();
        if(likes.contains(likeDTO.getId()))likes.remove(likeDTO.getId());
        else likes.add(likeDTO.getId());
        postRepository.save(p);
        
    }
    @Override
    public void addComment(Long id, CommentDTO commentDTO) throws FacebookException {
        Post p=postRepository.findById(id).orElseThrow(()->new FacebookException("Post not Found"));
        if(p.getComments()==null)p.setComments(new ArrayList<CommentDTO>());
        List<CommentDTO>comments=p.getComments();
        comments.add(commentDTO);
        p.setComments(comments);
        postRepository.save(p);
    }
    @Override
    public Integer getPostCount(Long id) throws FacebookException {
        return postRepository.countByUserId(id);
    }
    @Override
    public Integer getPublicOnlyPostCount(Long id) throws FacebookException {
        return postRepository.countByUserIdAndPrivacy(id, Privacy.PUBLIC);
    }
    @Override
    public Integer getNonPrivatePostCount(Long id) throws FacebookException {
        ArrayList<Privacy> privacy = new ArrayList<>();
        privacy.add(Privacy.PUBLIC);
        privacy.add(Privacy.FRIENDS);
        return postRepository.countByUserIdAndPrivacyIn(id, privacy);
    }
}
