package com.epitech.pgt2019.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UserConv.
 */
@Document(collection = "user_conv")
public class UserConv implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @DBRef
    @Field("conversations")
    @JsonIgnore
    private Set<Conversation> conversations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Conversation> getConversations() {
        return conversations;
    }

    public UserConv conversations(Set<Conversation> conversations) {
        this.conversations = conversations;
        return this;
    }

    public UserConv addConversation(Conversation conversation) {
        this.conversations.add(conversation);
        conversation.getUserConvs().add(this);
        return this;
    }

    public UserConv removeConversation(Conversation conversation) {
        this.conversations.remove(conversation);
        conversation.getUserConvs().remove(this);
        return this;
    }

    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserConv userConv = (UserConv) o;
        if (userConv.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userConv.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserConv{" +
            "id=" + getId() +
            "}";
    }
}
