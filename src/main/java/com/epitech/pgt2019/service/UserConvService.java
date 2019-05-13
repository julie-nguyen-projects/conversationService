package com.epitech.pgt2019.service;

import com.epitech.pgt2019.domain.UserConv;
import com.epitech.pgt2019.repository.UserConvRepository;
import com.epitech.pgt2019.service.dto.UserConvDTO;
import com.epitech.pgt2019.service.mapper.UserConvMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing UserConv.
 */
@Service
public class UserConvService {

    private final Logger log = LoggerFactory.getLogger(UserConvService.class);

    private final UserConvRepository userConvRepository;

    private final UserConvMapper userConvMapper;

    public UserConvService(UserConvRepository userConvRepository, UserConvMapper userConvMapper) {
        this.userConvRepository = userConvRepository;
        this.userConvMapper = userConvMapper;
    }

    /**
     * Save a userConv.
     *
     * @param userConvDTO the entity to save
     * @return the persisted entity
     */
    public UserConvDTO save(UserConvDTO userConvDTO) {
        log.debug("Request to save UserConv : {}", userConvDTO);
        UserConv userConv = userConvMapper.toEntity(userConvDTO);
        userConv = userConvRepository.save(userConv);
        return userConvMapper.toDto(userConv);
    }

    /**
     * Get all the userConvs.
     *
     * @return the list of entities
     */
    public List<UserConvDTO> findAll() {
        log.debug("Request to get all UserConvs");
        return userConvRepository.findAll().stream()
            .map(userConvMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one userConv by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<UserConvDTO> findOne(String id) {
        log.debug("Request to get UserConv : {}", id);
        return userConvRepository.findById(id)
            .map(userConvMapper::toDto);
    }

    /**
     * Delete the userConv by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete UserConv : {}", id);
        userConvRepository.deleteById(id);
    }
}
