package com.logiciel.weeklyshop.repository;

import com.logiciel.weeklyshop.domain.ShoppingItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShoppingItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {

}
