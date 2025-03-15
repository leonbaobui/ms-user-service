package com.twitter.ms.repository;

import java.util.List;

import com.twitter.ms.model.GraphUser;
import com.twitter.ms.repository.projection.UserProjection;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphUserRepository extends Neo4jRepository<GraphUser, Long> {
    @Query("""
        MATCH (me:User)-[:FOLLOWS]->(friend:User)-[:FOLLOWS]->(suggested:User)
        WHERE me.id = $userId AND NOT (me)-[:FOLLOWS]->(suggested)
        AND me <> suggested
        RETURN DISTINCT suggested
        LIMIT 5
    """)
    List<UserProjection> findSuggestedUsers(Long userId);
}
