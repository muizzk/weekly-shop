package com.logiciel.weeklyshop.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ShoppingList.
 */
@Entity
@Table(name = "shopping_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShoppingList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "owner")
    private String owner;

    @OneToMany(mappedBy = "shoppingList")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ShoppingItem> shoppingItems = new HashSet<>();

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

    public ShoppingList owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public ShoppingList shoppingItems(Set<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
        return this;
    }

    public ShoppingList addShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItems.add(shoppingItem);
        shoppingItem.setShoppingList(this);
        return this;
    }

    public ShoppingList removeShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItems.remove(shoppingItem);
        shoppingItem.setShoppingList(null);
        return this;
    }

    public void setShoppingItems(Set<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingList)) {
            return false;
        }
        return id != null && id.equals(((ShoppingList) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
            "id=" + getId() +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
