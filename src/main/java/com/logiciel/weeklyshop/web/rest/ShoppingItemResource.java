package com.logiciel.weeklyshop.web.rest;

import com.logiciel.weeklyshop.domain.ShoppingItem;
import com.logiciel.weeklyshop.repository.ShoppingItemRepository;
import com.logiciel.weeklyshop.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.logiciel.weeklyshop.domain.ShoppingItem}.
 */
@RestController
@RequestMapping("/api")
public class ShoppingItemResource {

    private final Logger log = LoggerFactory.getLogger(ShoppingItemResource.class);

    private static final String ENTITY_NAME = "shoppingItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShoppingItemRepository shoppingItemRepository;

    public ShoppingItemResource(ShoppingItemRepository shoppingItemRepository) {
        this.shoppingItemRepository = shoppingItemRepository;
    }

    /**
     * {@code POST  /shopping-items} : Create a new shoppingItem.
     *
     * @param shoppingItem the shoppingItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shoppingItem, or with status {@code 400 (Bad Request)} if the shoppingItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shopping-items")
    public ResponseEntity<ShoppingItem> createShoppingItem(@Valid @RequestBody ShoppingItem shoppingItem) throws URISyntaxException {
        log.debug("REST request to save ShoppingItem : {}", shoppingItem);
        if (shoppingItem.getId() != null) {
            throw new BadRequestAlertException("A new shoppingItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShoppingItem result = shoppingItemRepository.save(shoppingItem);
        return ResponseEntity.created(new URI("/api/shopping-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shopping-items} : Updates an existing shoppingItem.
     *
     * @param shoppingItem the shoppingItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingItem,
     * or with status {@code 400 (Bad Request)} if the shoppingItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shopping-items")
    public ResponseEntity<ShoppingItem> updateShoppingItem(@Valid @RequestBody ShoppingItem shoppingItem) throws URISyntaxException {
        log.debug("REST request to update ShoppingItem : {}", shoppingItem);
        if (shoppingItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShoppingItem result = shoppingItemRepository.save(shoppingItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shoppingItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shopping-items} : get all the shoppingItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shoppingItems in body.
     */
    @GetMapping("/shopping-items")
    public List<ShoppingItem> getAllShoppingItems() {
        log.debug("REST request to get all ShoppingItems");
        return shoppingItemRepository.findAll();
    }

    /**
     * {@code GET  /shopping-items/:id} : get the "id" shoppingItem.
     *
     * @param id the id of the shoppingItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shopping-items/{id}")
    public ResponseEntity<ShoppingItem> getShoppingItem(@PathVariable Long id) {
        log.debug("REST request to get ShoppingItem : {}", id);
        Optional<ShoppingItem> shoppingItem = shoppingItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shoppingItem);
    }

    /**
     * {@code DELETE  /shopping-items/:id} : delete the "id" shoppingItem.
     *
     * @param id the id of the shoppingItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shopping-items/{id}")
    public ResponseEntity<Void> deleteShoppingItem(@PathVariable Long id) {
        log.debug("REST request to delete ShoppingItem : {}", id);
        shoppingItemRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

}
