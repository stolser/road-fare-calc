package com.stolser.repository;

import com.stolser.entity.Road;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RoadRepository extends MongoRepository<Road, String> {
    @Query("{'postSystemIds': {$all: [?0, ?1]}}")
    List<Road> findByTwoPostSystemIds(String firstPostSystemId, String secondPostSystemId);
    Road findBySystemId(String systemId);
}
