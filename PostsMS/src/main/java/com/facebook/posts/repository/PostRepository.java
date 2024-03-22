package com.facebook.posts.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.facebook.posts.dto.Privacy;
import com.facebook.posts.entity.Post;

public interface PostRepository extends MongoRepository<Post, Long> {
    List<Post> findTop4ByUserIdOrderByTimestampDesc(Long id);
    Page<Post> findAllByUserIdNotAndPrivacyNot(Long id, Privacy privacySetting, Pageable pageable);
    List<Post> findAllByUserId(Long id, Sort sort);
    List<Post> findAllByUserIdAndPrivacyIn(Long id, List<Privacy> privacy, Sort sort);
    Integer countByUserId(Long id);
    Integer countByUserIdAndPrivacy(Long id, Privacy public1);
    Integer countByUserIdAndPrivacyIn(Long id, List<Privacy> privacy);
    List<Post> findAllByUserIdOrPrivacyOrUserIdInAndPrivacy(Long id,Privacy p ,List<Long> asList, Privacy privacy, Sort sort);
}