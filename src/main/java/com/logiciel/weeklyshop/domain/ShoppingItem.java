package com.logiciel.weeklyshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.logiciel.weeklyshop.domain.enumeration.Origin;

/**
 * A ShoppingItem.
 */
@Entity
@Table(name = "shopping_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShoppingItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "owner", nullable = false)
    private String owner;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "quantity")
    private String quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin")
    private Origin origin;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JsonIgnoreProperties("shoppingItems")
    private ShoppingList shoppingList;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("shoppingItems")
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public ShoppingItem owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public ShoppingItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public ShoppingItem quantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Origin getOrigin() {
        return origin;
    }

    public ShoppingItem origin(Origin origin) {
        this.origin = origin;
        return this;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public ShoppingItem deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public ShoppingItem shoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
        return this;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Category getCategory() {
        return category;
    }

    public ShoppingItem category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingItem)) {
            return false;
        }
        return id != null && id.equals(((ShoppingItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShoppingItem{" +
            "id=" + getId() +
            ", owner='" + getOwner() + "'" +
            ", name='" + getName() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", origin='" + getOrigin() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
