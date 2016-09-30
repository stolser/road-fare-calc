package com.stolser.repository;

import com.stolser.entity.Road;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoadRepository extends MongoRepository<Road, String> {
}
