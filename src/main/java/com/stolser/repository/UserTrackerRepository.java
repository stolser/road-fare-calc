package com.stolser.repository;

import com.stolser.entity.UserTracker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTrackerRepository extends MongoRepository<UserTracker, String> {
}
