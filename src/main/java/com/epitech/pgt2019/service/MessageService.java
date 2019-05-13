package com.epitech.pgt2019.service;

import com.epitech.pgt2019.domain.Message;
import com.epitech.pgt2019.repository.MessageRepository;
import com.epitech.pgt2019.service.dto.MessageDTO;
import com.epitech.pgt2019.service.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Message.
 */
@Service
public class MessageService {

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    /**
     * Save a message.
     *
     * @param messageDTO the entity to save
     * @return the persisted entity
     */
    public MessageDTO save(MessageDTO messageDTO) {
        log.debug("Request to save Message : {}", messageDTO);
        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    /**
     * Get all the messages.
     *
     * @return the list of entities
     */
    public List<MessageDTO> findAll() {
        log.debug("Request to get all Messages");
        return messageRepository.findAll().stream()
            .map(messageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one message by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<MessageDTO> findOne(String id) {
        log.debug("Request to get Message : {}", id);
        return messageRepository.findById(id)
            .map(messageMapper::toDto);
    }

    /**
     * Delete the message by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Message : {}", id);
        messageRepository.deleteById(id);
    }
}
