package com.facebook.user.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.facebook.user.dto.LoginDTO;
import com.facebook.user.dto.UserDTO;
import com.facebook.user.entity.SequenceId;
import com.facebook.user.entity.User;
import com.facebook.user.exception.FacebookException;
import com.facebook.user.repository.UserRepository;

@Service(value="userService")
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoOperations mongoOperation;
    private static final String HOSTING_SEQ_KEY = "hosting";
    private static final String USER_NOT_FOUND="Service.USER_NOT_FOUND";
    @Override
    public long getNextSequenceId(String key) throws FacebookException{
        Query query = new Query(Criteria.where("_id").is(key));
        Update update = new Update();
        update.inc("seq", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        SequenceId seqId = mongoOperation.findAndModify(query, update, options, SequenceId.class);
        if (seqId == null) {
            throw new FacebookException("Unable to get sequence id for key : " + key);
        }

        return seqId.getSeq();
    }
    @Override
    public Long registerUser(UserDTO userDTO) throws FacebookException {
        if (userDTO.getDob().until(LocalDate.now()).getYears() < 13)throw new FacebookException("Service.USER_INVALID_DOB");
        if(userRepository.findByUsername(userDTO.getUsername()).isPresent())throw new FacebookException("Service.USER_FOUND");
        userDTO.setId(getNextSequenceId(HOSTING_SEQ_KEY));
        User user=UserDTO.getEntity(userDTO);
        return userRepository.save(user).getId();
        
    }
    @Override
    public Long loginUser(LoginDTO loginDTO) throws FacebookException{
        User user=userRepository.findByUsername(loginDTO.getUsername()).orElseThrow(()->new FacebookException(USER_NOT_FOUND));
        if(!loginDTO.getPassword().equals(user.getPassword()))throw new FacebookException("Service.INVALID_CREDENTIALS");
        return user.getId();
    }
    @Override
    public void resetPassword(LoginDTO dto) throws FacebookException {
        User user = userRepository.findByUsernameAndEmail(dto.getUsername(), dto.getEmail()).orElseThrow(()->new FacebookException("Service.INVALID_USERNAME_OR_EMAIL"));
        user.setPassword(dto.getPassword());
        userRepository.save(user);
    }
    @Override
    public void updateUser(UserDTO userDTO) throws FacebookException {
        User user=userRepository.findByUsername(userDTO.getUsername()).orElseThrow(()->new FacebookException(USER_NOT_FOUND));
        user.setEmail(userDTO.getEmail());
        user.setDob(userDTO.getDob());
        user.setBio(userDTO.getBio());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setGender(userDTO.getGender());
        userRepository.save(user);
    }
    @Override
    public UserDTO getUser(Long id) throws FacebookException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) throw new FacebookException(USER_NOT_FOUND);
        
        User user = optionalUser.get();
        return UserDTO.entityToDTO(user);
    }
    @Override
    public List<UserDTO>getAllUsers(){
        List<User> l=userRepository.findAll();
        List<UserDTO>ans=new ArrayList<>();
        l.forEach(x->ans.add(UserDTO.entityToDTO(x)));
        return ans;
    } 
    @Override
    public UserDTO[] searchUsers(String searchInput) {
        Optional<User[]> optionalUsers = userRepository.findByUsernameLike(searchInput);
        if (!optionalUsers.isPresent()) return new UserDTO[0];
        
        User[] users = optionalUsers.get();
        UserDTO[] userDTOs = new UserDTO[users.length];
        
        for (int i=0; i<users.length; i++) {
            userDTOs[i] =UserDTO.entityToDTO(users[i]);
        }
        return userDTOs;
    }               
    @Override
    public String getUsername(Long userId) throws FacebookException {
        User user=userRepository.findById(userId).orElseThrow(()->new FacebookException(USER_NOT_FOUND));
        return user.getUsername();
    }
    @Override
    public Long getUserId(String username) throws FacebookException {
        User user=userRepository.findByUsername(username).orElseThrow(()->new FacebookException(USER_NOT_FOUND));
        return user.getId();
    }
    
}
