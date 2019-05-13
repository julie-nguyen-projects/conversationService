package com.epitech.pgt2019.service.mapper;

import com.epitech.pgt2019.domain.*;
import com.epitech.pgt2019.service.dto.MessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Message and its DTO MessageDTO.
 */
@Mapper(componentModel = "spring", uses = {ConversationMapper.class, UserConvMapper.class})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {

    @Mapping(source = "conversation.id", target = "conversationId")
    @Mapping(source = "userConv.id", target = "userConvId")
    MessageDTO toDto(Message message);

    @Mapping(source = "conversationId", target = "conversation")
    @Mapping(source = "userConvId", target = "userConv")
    Message toEntity(MessageDTO messageDTO);

    default Message fromId(String id) {
        if (id == null) {
            return null;
        }
        Message message = new Message();
        message.setId(id);
        return message;
    }
}
