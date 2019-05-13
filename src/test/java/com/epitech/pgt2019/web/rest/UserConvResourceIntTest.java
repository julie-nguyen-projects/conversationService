package com.epitech.pgt2019.web.rest;

import com.epitech.pgt2019.ConversationServiceApp;

import com.epitech.pgt2019.domain.UserConv;
import com.epitech.pgt2019.repository.UserConvRepository;
import com.epitech.pgt2019.service.UserConvService;
import com.epitech.pgt2019.service.dto.UserConvDTO;
import com.epitech.pgt2019.service.mapper.UserConvMapper;
import com.epitech.pgt2019.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.List;


import static com.epitech.pgt2019.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserConvResource REST controller.
 *
 * @see UserConvResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConversationServiceApp.class)
public class UserConvResourceIntTest {

    @Autowired
    private UserConvRepository userConvRepository;

    @Autowired
    private UserConvMapper userConvMapper;

    @Autowired
    private UserConvService userConvService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restUserConvMockMvc;

    private UserConv userConv;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserConvResource userConvResource = new UserConvResource(userConvService);
        this.restUserConvMockMvc = MockMvcBuilders.standaloneSetup(userConvResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConv createEntity() {
        UserConv userConv = new UserConv();
        return userConv;
    }

    @Before
    public void initTest() {
        userConvRepository.deleteAll();
        userConv = createEntity();
    }

    @Test
    public void createUserConv() throws Exception {
        int databaseSizeBeforeCreate = userConvRepository.findAll().size();

        // Create the UserConv
        UserConvDTO userConvDTO = userConvMapper.toDto(userConv);
        restUserConvMockMvc.perform(post("/api/user-convs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userConvDTO)))
            .andExpect(status().isCreated());

        // Validate the UserConv in the database
        List<UserConv> userConvList = userConvRepository.findAll();
        assertThat(userConvList).hasSize(databaseSizeBeforeCreate + 1);
        UserConv testUserConv = userConvList.get(userConvList.size() - 1);
    }

    @Test
    public void createUserConvWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userConvRepository.findAll().size();

        // Create the UserConv with an existing ID
        userConv.setId("existing_id");
        UserConvDTO userConvDTO = userConvMapper.toDto(userConv);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserConvMockMvc.perform(post("/api/user-convs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userConvDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserConv in the database
        List<UserConv> userConvList = userConvRepository.findAll();
        assertThat(userConvList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllUserConvs() throws Exception {
        // Initialize the database
        userConvRepository.save(userConv);

        // Get all the userConvList
        restUserConvMockMvc.perform(get("/api/user-convs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userConv.getId())));
    }
    
    @Test
    public void getUserConv() throws Exception {
        // Initialize the database
        userConvRepository.save(userConv);

        // Get the userConv
        restUserConvMockMvc.perform(get("/api/user-convs/{id}", userConv.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userConv.getId()));
    }

    @Test
    public void getNonExistingUserConv() throws Exception {
        // Get the userConv
        restUserConvMockMvc.perform(get("/api/user-convs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateUserConv() throws Exception {
        // Initialize the database
        userConvRepository.save(userConv);

        int databaseSizeBeforeUpdate = userConvRepository.findAll().size();

        // Update the userConv
        UserConv updatedUserConv = userConvRepository.findById(userConv.getId()).get();
        UserConvDTO userConvDTO = userConvMapper.toDto(updatedUserConv);

        restUserConvMockMvc.perform(put("/api/user-convs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userConvDTO)))
            .andExpect(status().isOk());

        // Validate the UserConv in the database
        List<UserConv> userConvList = userConvRepository.findAll();
        assertThat(userConvList).hasSize(databaseSizeBeforeUpdate);
        UserConv testUserConv = userConvList.get(userConvList.size() - 1);
    }

    @Test
    public void updateNonExistingUserConv() throws Exception {
        int databaseSizeBeforeUpdate = userConvRepository.findAll().size();

        // Create the UserConv
        UserConvDTO userConvDTO = userConvMapper.toDto(userConv);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConvMockMvc.perform(put("/api/user-convs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userConvDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserConv in the database
        List<UserConv> userConvList = userConvRepository.findAll();
        assertThat(userConvList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteUserConv() throws Exception {
        // Initialize the database
        userConvRepository.save(userConv);

        int databaseSizeBeforeDelete = userConvRepository.findAll().size();

        // Delete the userConv
        restUserConvMockMvc.perform(delete("/api/user-convs/{id}", userConv.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserConv> userConvList = userConvRepository.findAll();
        assertThat(userConvList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserConv.class);
        UserConv userConv1 = new UserConv();
        userConv1.setId("id1");
        UserConv userConv2 = new UserConv();
        userConv2.setId(userConv1.getId());
        assertThat(userConv1).isEqualTo(userConv2);
        userConv2.setId("id2");
        assertThat(userConv1).isNotEqualTo(userConv2);
        userConv1.setId(null);
        assertThat(userConv1).isNotEqualTo(userConv2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserConvDTO.class);
        UserConvDTO userConvDTO1 = new UserConvDTO();
        userConvDTO1.setId("id1");
        UserConvDTO userConvDTO2 = new UserConvDTO();
        assertThat(userConvDTO1).isNotEqualTo(userConvDTO2);
        userConvDTO2.setId(userConvDTO1.getId());
        assertThat(userConvDTO1).isEqualTo(userConvDTO2);
        userConvDTO2.setId("id2");
        assertThat(userConvDTO1).isNotEqualTo(userConvDTO2);
        userConvDTO1.setId(null);
        assertThat(userConvDTO1).isNotEqualTo(userConvDTO2);
    }
}
