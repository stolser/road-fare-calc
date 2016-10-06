package com.stolser.repository;

import com.stolser.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findBySystemId(long systemId);
    List<User> findByFirstNameAndLastNameAllIgnoreCase(String firstName, String lastName);
}
