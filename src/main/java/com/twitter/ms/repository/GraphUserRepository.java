package com.twitter.ms.repository;

import java.util.List;

import com.twitter.ms.model.GraphUser;
import com.twitter.ms.repository.projection.UserProjection;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphUserRepository extends Neo4jRepository<GraphUser, Long> {
    @Query("MATCH (me:User)\n" +
           "WHERE me.id = $userId\n" +
           "    OPTIONAL MATCH (me)-[:FOLLOWS]->(friend:User)-[:FOLLOWS]->(suggested:User)\n" +
           "        WHERE NOT (me)-[:FOLLOWS]->(suggested) AND me <> suggested\n" +
           "        WITH me, collect(DISTINCT suggested) AS suggestedFriends\n" +
           "    OPTIONAL MATCH (extra:User)\n" +
           "        WHERE NOT (me)-[:FOLLOWS]->(extra) AND me <> extra\n" +
           "        WITH me, suggestedFriends, collect(DISTINCT extra) AS extraUsers\n" +
           "        WITH suggestedFriends + extraUsers[..(5 - size(suggestedFriends))] AS finalSuggestions\n" +
           "        UNWIND finalSuggestions AS finalSuggested\n" +
           "RETURN DISTINCT finalSuggested\n" +
           "LIMIT 5\n")
    List<UserProjection> findTop5SuggestedUsers(Long userId);

}
