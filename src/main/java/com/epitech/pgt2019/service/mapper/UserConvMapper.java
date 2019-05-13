package com.epitech.pgt2019.service.mapper;

import com.epitech.pgt2019.domain.*;
import com.epitech.pgt2019.service.dto.UserConvDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserConv and its DTO UserConvDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserConvMapper extends EntityMapper<UserConvDTO, UserConv> {


    @Mapping(target = "conversations", ignore = true)
    UserConv toEntity(UserConvDTO userConvDTO);

    default UserConv fromId(String id) {
        if (id == null) {
            return null;
        }
        UserConv userConv = new UserConv();
        userConv.setId(id);
        return userConv;
    }
}
