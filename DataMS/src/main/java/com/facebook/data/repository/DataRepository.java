package com.facebook.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.facebook.data.entity.Data;

public interface DataRepository extends MongoRepository<Data, String>{
    
}
