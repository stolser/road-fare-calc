package com.stolser.repository;

import com.stolser.entity.TrafficPost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrafficPostRepository extends MongoRepository<TrafficPost, String> {
}
