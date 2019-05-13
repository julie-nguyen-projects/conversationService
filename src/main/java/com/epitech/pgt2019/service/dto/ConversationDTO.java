package com.epitech.pgt2019.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Conversation entity.
 */
public class ConversationDTO implements Serializable {

    private String id;


    private Set<UserConvDTO> userConvs = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<UserConvDTO> getUserConvs() {
        return userConvs;
    }

    public void setUserConvs(Set<UserConvDTO> userConvs) {
        this.userConvs = userConvs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConversationDTO conversationDTO = (ConversationDTO) o;
        if (conversationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conversationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConversationDTO{" +
            "id=" + getId() +
            "}";
    }
}
