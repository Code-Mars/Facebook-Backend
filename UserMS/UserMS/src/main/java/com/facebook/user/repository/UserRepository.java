package com.facebook.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.facebook.user.entity.User;

public interface UserRepository extends MongoRepository<User, Long>{
    Optional<User>findByUsername(String username);
    Optional<User> findByUsernameAndEmail(String username, String email);
    Optional<User[]> findByUsernameLike(String searchInput);
}
