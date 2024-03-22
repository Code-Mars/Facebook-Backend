package com.facebook.posts.controller;

import java.time.LocalDateTime;
import java.util.List;

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

import com.facebook.posts.dto.CommentDTO;
import com.facebook.posts.dto.HttpMessageDTO;
import com.facebook.posts.dto.LikeDTO;
import com.facebook.posts.dto.PostDTO;
import com.facebook.posts.exception.FacebookException;
import com.facebook.posts.service.PostService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@CrossOrigin("*")
@RestController
@Validated
@RequestMapping("/posts")
public class PostAPI {

    @Autowired
    private PostService postService;
    @Autowired
    private Environment environment;

    @PostMapping(value = "/create")
    public ResponseEntity<HttpMessageDTO> createPost(@Valid @RequestBody PostDTO postDTO) throws FacebookException {
        postService.createPost(postDTO);
        String message = environment.getProperty("PostAPI.POST_SAVE_SUCCESSFUL");
        return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<HttpMessageDTO> updatePost(@Valid @RequestBody PostDTO postDTO) throws FacebookException {
        if (postDTO.getId() == null)
            throw new FacebookException("Service.POST_ID_NOT_PRESENT");
        postService.updatePost(postDTO);
        String message = environment.getProperty("PostAPI.POST_UPDATE_SUCCESSFUL");
        return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<HttpMessageDTO> deletePost(@Valid @RequestBody Long id) throws FacebookException {
        if (id == null)
            throw new FacebookException("Service.POST_ID_NOT_PRESENT");
        postService.deletePost(id);
        String message = environment.getProperty("PostAPI.POST_DELETE_SUCCESSFUL");
        return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
    }

    @PostMapping(value = "/like")
    public ResponseEntity<HttpMessageDTO> likePost(@Valid @RequestBody LikeDTO likeDTO) throws FacebookException {
        if (likeDTO.getId() == null)
            throw new FacebookException("Service.POST_ID_NOT_PRESENT");
        postService.likePost(likeDTO);
        String message = environment.getProperty("PostAPI.POST_LIKED_SUCCESSFUL");
        return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
    }

    @PostMapping(value = "/comment/{id}")
    public ResponseEntity<HttpMessageDTO> addComment(@PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO)
            throws FacebookException {
        commentDTO.setTimestamp(LocalDateTime.now());
        postService.addComment(id, commentDTO);
        String message = environment.getProperty("PostAPI.COMMENT_SUCCESSFUL");
        return new ResponseEntity<>(new HttpMessageDTO(message), HttpStatus.OK);
    }

    @PostMapping(value = "/recent")
    public ResponseEntity<List<PostDTO>> getMyRecentPosts(@NotNull(message = "{userid.absent}") @RequestBody Long id)
            throws FacebookException {
        List<PostDTO> postDTOs = postService.getMyRecentPosts(id);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/recent/{id}")
    public ResponseEntity<List<PostDTO>> getRecentPosts(@NotNull(message = "{userid.absent}") @PathVariable Long id,
            @RequestBody String s)
            throws FacebookException {
        List<Long> friends = getFriendList(id);
        List<PostDTO> postDTOs = postService.getRecentPosts(id, friends, s);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/myposts/{id}")
    public ResponseEntity<List<PostDTO>> getUserPosts(@NotNull(message = "{userid.absent}") @PathVariable Long id,
            @RequestBody String s)
            throws FacebookException {
        List<PostDTO> postDTOs = postService.getUserPosts(id, s);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/friendposts/{id}/{friendId}")
    public ResponseEntity<List<PostDTO>> getFriendPosts(@NotNull(message = "{userid.absent}") @PathVariable Long id,
            @NotNull(message = "{userid.absent}") @PathVariable Long friendId, @RequestBody String s)
            throws FacebookException {
        Boolean isFriend = checkExistingFriend(id, friendId);
        List<PostDTO> postDTOs = postService.getFriendPosts(id, isFriend, s);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/count/all")
    public ResponseEntity<HttpMessageDTO> getPostCount(@NotNull(message = "{userid.absent}") @RequestBody Long id)
            throws FacebookException {
        Integer postCount = postService.getPostCount(id);
        return new ResponseEntity<>(new HttpMessageDTO(postCount.toString()), HttpStatus.OK);
    }

    @PostMapping(value = "/count/public")
    public ResponseEntity<HttpMessageDTO> getPublicOnlyPostCount(
            @NotNull(message = "{userid.absent}") @RequestBody Long id) throws FacebookException {
        Integer postCount = postService.getPublicOnlyPostCount(id);
        return new ResponseEntity<>(new HttpMessageDTO(postCount.toString()), HttpStatus.OK);
    }

    @PostMapping(value = "/count/nonprivate")
    public ResponseEntity<HttpMessageDTO> getNonPrivatePostCount(
            @NotNull(message = "{userid.absent}") @RequestBody Long id) throws FacebookException {
        Integer postCount = postService.getNonPrivatePostCount(id);
        return new ResponseEntity<>(new HttpMessageDTO(postCount.toString()), HttpStatus.OK);
    }

    public Boolean checkExistingFriend(Long id, Long friend) throws FacebookException {
        String url = "http://localhost:9500/friends/existing/friend/" + friend;
        HttpMessageDTO messageDTO = WebClient.create().post().uri(url).bodyValue(id).retrieve()
                .bodyToMono(HttpMessageDTO.class).block();
        if (messageDTO == null)
            throw new FacebookException("PostAPI.USER_ID_NOT_FOUND");
        return Boolean.valueOf(messageDTO.getMessage());
    }

    public String getUsernameByUserId(Long id) throws FacebookException {
        String url = "http://localhost:9100/users/get/username";
        String username = WebClient.create().post().uri(url).bodyValue(id).retrieve().bodyToMono(String.class).block();
        if (username == null)
            throw new FacebookException("PostAPI.USER_NAME_NOT_FOUND");
        return username;
    }

    public List<Long> getFriendList(Long id) {
        String url = "http://localhost:9500/friends/get";
        return WebClient.create().post().uri(url).bodyValue(id).retrieve().bodyToMono(List.class).block();
    }

    public Long getUserIdByUsername(String username) {
        String url = "http://localhost:9100/users/get/userid";
        return WebClient.create().post().uri(url).bodyValue(username).retrieve().bodyToMono(Long.class).block();
    }

}
