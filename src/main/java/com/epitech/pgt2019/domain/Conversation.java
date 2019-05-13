package com.epitech.pgt2019.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Conversation.
 */
@Document(collection = "conversation")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @DBRef
    @Field("userConvs")
    private Set<UserConv> userConvs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<UserConv> getUserConvs() {
        return userConvs;
    }

    public Conversation userConvs(Set<UserConv> userConvs) {
        this.userConvs = userConvs;
        return this;
    }

    public Conversation addUserConv(UserConv userConv) {
        this.userConvs.add(userConv);
        userConv.getConversations().add(this);
        return this;
    }

    public Conversation removeUserConv(UserConv userConv) {
        this.userConvs.remove(userConv);
        userConv.getConversations().remove(this);
        return this;
    }

    public void setUserConvs(Set<UserConv> userConvs) {
        this.userConvs = userConvs;
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
        Conversation conversation = (Conversation) o;
        if (conversation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conversation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Conversation{" +
            "id=" + getId() +
            "}";
    }
}
