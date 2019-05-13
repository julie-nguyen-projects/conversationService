package com.epitech.pgt2019.repository;

import com.epitech.pgt2019.domain.Conversation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Conversation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {

}
