package com.logiciel.weeklyshop.repository;

import com.logiciel.weeklyshop.domain.Staple;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Staple entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StapleRepository extends JpaRepository<Staple, Long> {

}
