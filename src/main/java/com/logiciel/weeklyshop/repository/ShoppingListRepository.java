package com.logiciel.weeklyshop.repository;

import com.logiciel.weeklyshop.domain.ShoppingList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShoppingList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

}
