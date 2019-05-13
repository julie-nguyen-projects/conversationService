package com.epitech.pgt2019.web.rest;

import com.epitech.pgt2019.ConversationServiceApp;

import com.epitech.pgt2019.domain.Conversation;
import com.epitech.pgt2019.domain.UserConv;
import com.epitech.pgt2019.repository.ConversationRepository;
import com.epitech.pgt2019.service.ConversationService;
import com.epitech.pgt2019.service.dto.ConversationDTO;
import com.epitech.pgt2019.service.mapper.ConversationMapper;
import com.epitech.pgt2019.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;


import static com.epitech.pgt2019.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ConversationResource REST controller.
 *
 * @see ConversationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConversationServiceApp.class)
public class ConversationResourceIntTest {

    @Autowired
    private ConversationRepository conversationRepository;

    @Mock
    private ConversationRepository conversationRepositoryMock;

    @Autowired
    private ConversationMapper conversationMapper;

    @Mock
    private ConversationService conversationServiceMock;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restConversationMockMvc;

    private Conversation conversation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConversationResource conversationResource = new ConversationResource(conversationService);
        this.restConversationMockMvc = MockMvcBuilders.standaloneSetup(conversationResource)
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
    public static Conversation createEntity() {
        Conversation conversation = new Conversation();
        // Add required entity
        UserConv userConv = UserConvResourceIntTest.createEntity();
        userConv.setId("fixed-id-for-tests");
        conversation.getUserConvs().add(userConv);
        return conversation;
    }

    @Before
    public void initTest() {
        conversationRepository.deleteAll();
        conversation = createEntity();
    }

    @Test
    public void createConversation() throws Exception {
        int databaseSizeBeforeCreate = conversationRepository.findAll().size();

        // Create the Conversation
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);
        restConversationMockMvc.perform(post("/api/conversations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conversationDTO)))
            .andExpect(status().isCreated());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeCreate + 1);
        Conversation testConversation = conversationList.get(conversationList.size() - 1);
    }

    @Test
    public void createConversationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = conversationRepository.findAll().size();

        // Create the Conversation with an existing ID
        conversation.setId("existing_id");
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConversationMockMvc.perform(post("/api/conversations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conversationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllConversations() throws Exception {
        // Initialize the database
        conversationRepository.save(conversation);

        // Get all the conversationList
        restConversationMockMvc.perform(get("/api/conversations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversation.getId())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllConversationsWithEagerRelationshipsIsEnabled() throws Exception {
        ConversationResource conversationResource = new ConversationResource(conversationServiceMock);
        when(conversationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restConversationMockMvc = MockMvcBuilders.standaloneSetup(conversationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConversationMockMvc.perform(get("/api/conversations?eagerload=true"))
        .andExpect(status().isOk());

        verify(conversationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllConversationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ConversationResource conversationResource = new ConversationResource(conversationServiceMock);
            when(conversationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restConversationMockMvc = MockMvcBuilders.standaloneSetup(conversationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConversationMockMvc.perform(get("/api/conversations?eagerload=true"))
        .andExpect(status().isOk());

            verify(conversationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getConversation() throws Exception {
        // Initialize the database
        conversationRepository.save(conversation);

        // Get the conversation
        restConversationMockMvc.perform(get("/api/conversations/{id}", conversation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conversation.getId()));
    }

    @Test
    public void getNonExistingConversation() throws Exception {
        // Get the conversation
        restConversationMockMvc.perform(get("/api/conversations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateConversation() throws Exception {
        // Initialize the database
        conversationRepository.save(conversation);

        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();

        // Update the conversation
        Conversation updatedConversation = conversationRepository.findById(conversation.getId()).get();
        ConversationDTO conversationDTO = conversationMapper.toDto(updatedConversation);

        restConversationMockMvc.perform(put("/api/conversations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conversationDTO)))
            .andExpect(status().isOk());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
        Conversation testConversation = conversationList.get(conversationList.size() - 1);
    }

    @Test
    public void updateNonExistingConversation() throws Exception {
        int databaseSizeBeforeUpdate = conversationRepository.findAll().size();

        // Create the Conversation
        ConversationDTO conversationDTO = conversationMapper.toDto(conversation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversationMockMvc.perform(put("/api/conversations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conversationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Conversation in the database
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteConversation() throws Exception {
        // Initialize the database
        conversationRepository.save(conversation);

        int databaseSizeBeforeDelete = conversationRepository.findAll().size();

        // Delete the conversation
        restConversationMockMvc.perform(delete("/api/conversations/{id}", conversation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Conversation> conversationList = conversationRepository.findAll();
        assertThat(conversationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conversation.class);
        Conversation conversation1 = new Conversation();
        conversation1.setId("id1");
        Conversation conversation2 = new Conversation();
        conversation2.setId(conversation1.getId());
        assertThat(conversation1).isEqualTo(conversation2);
        conversation2.setId("id2");
        assertThat(conversation1).isNotEqualTo(conversation2);
        conversation1.setId(null);
        assertThat(conversation1).isNotEqualTo(conversation2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConversationDTO.class);
        ConversationDTO conversationDTO1 = new ConversationDTO();
        conversationDTO1.setId("id1");
        ConversationDTO conversationDTO2 = new ConversationDTO();
        assertThat(conversationDTO1).isNotEqualTo(conversationDTO2);
        conversationDTO2.setId(conversationDTO1.getId());
        assertThat(conversationDTO1).isEqualTo(conversationDTO2);
        conversationDTO2.setId("id2");
        assertThat(conversationDTO1).isNotEqualTo(conversationDTO2);
        conversationDTO1.setId(null);
        assertThat(conversationDTO1).isNotEqualTo(conversationDTO2);
    }
}
