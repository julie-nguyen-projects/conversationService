package com.epitech.pgt2019.repository;

import com.epitech.pgt2019.domain.Message;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

}
