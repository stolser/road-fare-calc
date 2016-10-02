package com.stolser.repository;

import com.stolser.entity.Road;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoadRepository extends MongoRepository<Road, String> {
    List<Road> findByPostSystemIdsIn(List<String> postSystemIds);
}
