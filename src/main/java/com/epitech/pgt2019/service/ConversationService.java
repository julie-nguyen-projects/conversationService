package com.epitech.pgt2019.service;

import com.epitech.pgt2019.domain.Conversation;
import com.epitech.pgt2019.repository.ConversationRepository;
import com.epitech.pgt2019.service.dto.ConversationDTO;
import com.epitech.pgt2019.service.mapper.ConversationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Conversation.
 */
@Service
public class ConversationService {

    private final Logger log = LoggerFactory.getLogger(ConversationService.class);

    private final ConversationRepository conversationRepository;

    private final ConversationMapper conversationMapper;

    public ConversationService(ConversationRepository conversationRepository, ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
    }

    /**
     * Save a conversation.
     *
     * @param conversationDTO the entity to save
     * @return the persisted entity
     */
    public ConversationDTO save(ConversationDTO conversationDTO) {
        log.debug("Request to save Conversation : {}", conversationDTO);
        Conversation conversation = conversationMapper.toEntity(conversationDTO);
        conversation = conversationRepository.save(conversation);
        return conversationMapper.toDto(conversation);
    }

    /**
     * Get all the conversations.
     *
     * @return the list of entities
     */
    public List<ConversationDTO> findAll() {
        log.debug("Request to get all Conversations");
        return conversationRepository.findAllWithEagerRelationships().stream()
            .map(conversationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the Conversation with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ConversationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return conversationRepository.findAllWithEagerRelationships(pageable).map(conversationMapper::toDto);
    }
    

    /**
     * Get one conversation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<ConversationDTO> findOne(String id) {
        log.debug("Request to get Conversation : {}", id);
        return conversationRepository.findOneWithEagerRelationships(id)
            .map(conversationMapper::toDto);
    }

    /**
     * Delete the conversation by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Conversation : {}", id);
        conversationRepository.deleteById(id);
    }
}
