package com.facebook.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.facebook.user.dto.FriendRequestDTO;
import com.facebook.user.dto.HttpMessageDTO;
import com.facebook.user.dto.LoginDTO;
import com.facebook.user.dto.UserDTO;
import com.facebook.user.exception.FacebookException;
import com.facebook.user.service.UserService;
import com.facebook.user.utility.ErrorInfo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@CrossOrigin
@RequestMapping("/users")
@Validated
public class UserAPI {
    @Autowired
    private UserService userService;
    @Autowired
    private Environment environment;
    
    @PostMapping(value = "/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserDTO userDTO) throws FacebookException {
        Long id=userService.registerUser(userDTO);
        userDTO.setId(id);
        this.registerFriendList(id);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);       
    }
    @CircuitBreaker(name="userService", fallbackMethod="loginUserFallback")
    @PostMapping(value="/login")
    public ResponseEntity<Long>loginUser(@RequestBody @Valid LoginDTO loginDTO) throws FacebookException{
        return new ResponseEntity<>(userService.loginUser(loginDTO), HttpStatus.ACCEPTED);
    }
    @PostMapping(value="/resetPassword")
    public ResponseEntity<HttpMessageDTO>resetPassword(@RequestBody @Valid LoginDTO loginDTO) throws FacebookException{
        userService.resetPassword(loginDTO);
        String msg=environment.getProperty("UserAPI.PASSWORD_RESET_SUCCESSFUL");
        return new ResponseEntity<>(new HttpMessageDTO(msg), HttpStatus.OK);
    }
    @PostMapping(value="/update")
    public ResponseEntity<HttpMessageDTO> updateUser(@RequestBody @Valid UserDTO userDTO) throws FacebookException {
        userService.updateUser(userDTO);
        String msg=environment.getProperty("UserAPI.USER_UPDATED_SUCCESSFULLY");
        return new ResponseEntity<>( new HttpMessageDTO(msg), HttpStatus.OK);       
    }
    @PostMapping(value = "/get")
    public ResponseEntity<UserDTO> getProfile(@NotNull(message = "{userid.absent}") @RequestBody Long id) throws FacebookException {
        UserDTO userDTO = userService.getUser(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @PostMapping(value = "/search")
    public ResponseEntity<UserDTO[]> searchProfiles(@NotNull(message = "{username.absent}") @RequestBody String searchInput) {
        UserDTO[] userDTOs = userService.searchUsers(searchInput);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }
    @PostMapping(value = "/get/username")
    public ResponseEntity<String> getUsername(@NotNull(message = "{userid.absent}") @RequestBody Long userId) throws FacebookException {
        String username = userService.getUsername(userId);
        return new ResponseEntity<>(username, HttpStatus.CREATED);
    }
    @PostMapping(value = "/get/userid")
    public ResponseEntity<Long> getUserId(@NotNull(message = "{username.absent}") @RequestBody String username) throws FacebookException {
        Long userId = userService.getUserId(username);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }
    @PostMapping(value="/suggestions")
    public ResponseEntity<List<UserDTO>>getSuggestions(@NotNull(message = "{userid.absent}") @RequestBody Long id){
        List<FriendRequestDTO>fr=WebClient.create().post().uri("http://localhost:9400/requests/sentFrom/"+id).retrieve().bodyToFlux(FriendRequestDTO.class).collectList().block();
        List<Long>fl=WebClient.create().post().uri("http://localhost:9500/friends/get").bodyValue(id).retrieve().bodyToFlux(Long.class).collectList().block();
        List<Long>friends=new ArrayList<>();
        if(fr!=null)fr.forEach(x->friends.add(Long.valueOf(x.getSentTo())));
        friends.addAll(fl);
        friends.add(id);
        List<UserDTO> users=userService.getAllUsers();
        List<UserDTO>l=users.stream().filter(x->!friends.contains(x.getId())).toList();
        return new ResponseEntity<>(l, HttpStatus.OK);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>>getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    public String registerFriendList(Long id) throws FacebookException {
        String url = "http://localhost:9500/friends/register";
        HttpMessageDTO  messageDTO=WebClient.create().post().uri(url).bodyValue(id).retrieve().bodyToMono(HttpMessageDTO.class).block();
        if (messageDTO == null) throw new FacebookException("UserAPI.USER_FRIEND_LIST_NOT_FOUND");
        return messageDTO.getMessage();
    }
    public ResponseEntity<ErrorInfo> loginUserFallback(LoginDTO loginDTO, Throwable throwable) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
        
        if (throwable.getMessage().contains("CircuitBreaker")) {
            errorInfo.setErrorMessage(throwable.getMessage());
        } else {
            errorInfo.setErrorMessage(environment.getProperty(throwable.getMessage()));
        }
        
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}
