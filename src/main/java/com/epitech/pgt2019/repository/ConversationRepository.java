package com.epitech.pgt2019.repository;

import com.epitech.pgt2019.domain.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Conversation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    @Query("{}")
    Page<Conversation> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Conversation> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Conversation> findOneWithEagerRelationships(String id);

}
