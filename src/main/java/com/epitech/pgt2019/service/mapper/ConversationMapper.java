package com.epitech.pgt2019.service.mapper;

import com.epitech.pgt2019.domain.*;
import com.epitech.pgt2019.service.dto.ConversationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Conversation and its DTO ConversationDTO.
 */
@Mapper(componentModel = "spring", uses = {UserConvMapper.class})
public interface ConversationMapper extends EntityMapper<ConversationDTO, Conversation> {



    default Conversation fromId(String id) {
        if (id == null) {
            return null;
        }
        Conversation conversation = new Conversation();
        conversation.setId(id);
        return conversation;
    }
}
