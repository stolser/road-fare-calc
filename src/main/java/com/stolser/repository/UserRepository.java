package com.stolser.repository;

import com.stolser.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findBySystemId(long systemId);
}
