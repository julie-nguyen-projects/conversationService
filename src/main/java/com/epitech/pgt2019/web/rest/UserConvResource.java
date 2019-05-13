package com.epitech.pgt2019.web.rest;
import com.epitech.pgt2019.service.UserConvService;
import com.epitech.pgt2019.web.rest.errors.BadRequestAlertException;
import com.epitech.pgt2019.web.rest.util.HeaderUtil;
import com.epitech.pgt2019.service.dto.UserConvDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserConv.
 */
@RestController
@RequestMapping("/api")
public class UserConvResource {

    private final Logger log = LoggerFactory.getLogger(UserConvResource.class);

    private static final String ENTITY_NAME = "conversationServiceUserConv";

    private final UserConvService userConvService;

    public UserConvResource(UserConvService userConvService) {
        this.userConvService = userConvService;
    }

    /**
     * POST  /user-convs : Create a new userConv.
     *
     * @param userConvDTO the userConvDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userConvDTO, or with status 400 (Bad Request) if the userConv has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-convs")
    public ResponseEntity<UserConvDTO> createUserConv(@RequestBody UserConvDTO userConvDTO) throws URISyntaxException {
        log.debug("REST request to save UserConv : {}", userConvDTO);
        if (userConvDTO.getId() != null) {
            throw new BadRequestAlertException("A new userConv cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserConvDTO result = userConvService.save(userConvDTO);
        return ResponseEntity.created(new URI("/api/user-convs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-convs : Updates an existing userConv.
     *
     * @param userConvDTO the userConvDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userConvDTO,
     * or with status 400 (Bad Request) if the userConvDTO is not valid,
     * or with status 500 (Internal Server Error) if the userConvDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-convs")
    public ResponseEntity<UserConvDTO> updateUserConv(@RequestBody UserConvDTO userConvDTO) throws URISyntaxException {
        log.debug("REST request to update UserConv : {}", userConvDTO);
        if (userConvDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserConvDTO result = userConvService.save(userConvDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userConvDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-convs : get all the userConvs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userConvs in body
     */
    @GetMapping("/user-convs")
    public List<UserConvDTO> getAllUserConvs() {
        log.debug("REST request to get all UserConvs");
        return userConvService.findAll();
    }

    /**
     * GET  /user-convs/:id : get the "id" userConv.
     *
     * @param id the id of the userConvDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userConvDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-convs/{id}")
    public ResponseEntity<UserConvDTO> getUserConv(@PathVariable String id) {
        log.debug("REST request to get UserConv : {}", id);
        Optional<UserConvDTO> userConvDTO = userConvService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userConvDTO);
    }

    /**
     * DELETE  /user-convs/:id : delete the "id" userConv.
     *
     * @param id the id of the userConvDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-convs/{id}")
    public ResponseEntity<Void> deleteUserConv(@PathVariable String id) {
        log.debug("REST request to delete UserConv : {}", id);
        userConvService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
