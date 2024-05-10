
package com.pi.farmease.dao;



import com.pi.farmease.entities.Conversation;
import com.pi.farmease.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,Integer> {
    @Query("SELECT c FROM Conversation c WHERE c.user1 = :user OR c.user2 = :user")
    List<Conversation> findByUser1OrUser2(User user);
    @Query(value = "SELECT * FROM conversation WHERE user1_id = :#{#user1.id} AND user2_id = :#{#user2.id}", nativeQuery = true)
    Optional<Conversation> findByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);

}

