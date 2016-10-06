package com.stolser.repository;

import com.stolser.entity.User;
import com.stolser.entity.UserTracker;
import com.stolser.entity.UserTrackerStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserTrackerRepository extends MongoRepository<UserTracker, String> {
    List<UserTracker> findByUser(User user);
    List<UserTracker> findByStatus(UserTrackerStatus status);
    List<UserTracker> findByStatusIsNot(UserTrackerStatus status);
    UserTracker findByUserAndStatusIsNot(User user, UserTrackerStatus status);
}
