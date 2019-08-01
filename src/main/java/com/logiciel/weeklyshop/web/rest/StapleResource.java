package com.logiciel.weeklyshop.web.rest;

import com.logiciel.weeklyshop.domain.Staple;
import com.logiciel.weeklyshop.repository.StapleRepository;
import com.logiciel.weeklyshop.repository.search.StapleSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.logiciel.weeklyshop.domain.Staple}.
 */
@RestController
@RequestMapping("/api")
public class StapleResource {

    private final Logger log = LoggerFactory.getLogger(StapleResource.class);

    private static final String ENTITY_NAME = "staple";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StapleRepository stapleRepository;

    private final StapleSearchRepository stapleSearchRepository;

    public StapleResource(StapleRepository stapleRepository, StapleSearchRepository stapleSearchRepository) {
        this.stapleRepository = stapleRepository;
        this.stapleSearchRepository = stapleSearchRepository;
    }

    /**
     * {@code POST  /staples} : Create a new staple.
     *
     * @param staple the staple to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staple, or with status {@code 400 (Bad Request)} if the staple has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/staples")
    public ResponseEntity<Staple> createStaple(@Valid @RequestBody Staple staple) throws URISyntaxException {
        log.debug("REST request to save Staple : {}", staple);
        if (staple.getId() != null) {
            throw new BadRequestAlertException("A new staple cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Staple result = stapleRepository.save(staple);
        stapleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/staples/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /staples} : Updates an existing staple.
     *
     * @param staple the staple to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staple,
     * or with status {@code 400 (Bad Request)} if the staple is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staple couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/staples")
    public ResponseEntity<Staple> updateStaple(@Valid @RequestBody Staple staple) throws URISyntaxException {
        log.debug("REST request to update Staple : {}", staple);
        if (staple.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Staple result = stapleRepository.save(staple);
        stapleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, staple.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /staples} : get all the staples.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staples in body.
     */
    @GetMapping("/staples")
    public List<Staple> getAllStaples() {
        log.debug("REST request to get all Staples");
        return stapleRepository.findAll();
    }

    /**
     * {@code GET  /staples/:id} : get the "id" staple.
     *
     * @param id the id of the staple to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staple, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/staples/{id}")
    public ResponseEntity<Staple> getStaple(@PathVariable Long id) {
        log.debug("REST request to get Staple : {}", id);
        Optional<Staple> staple = stapleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(staple);
    }

    /**
     * {@code DELETE  /staples/:id} : delete the "id" staple.
     *
     * @param id the id of the staple to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/staples/{id}")
    public ResponseEntity<Void> deleteStaple(@PathVariable Long id) {
        log.debug("REST request to delete Staple : {}", id);
        stapleRepository.deleteById(id);
        stapleSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/staples?query=:query} : search for the staple corresponding
     * to the query.
     *
     * @param query the query of the staple search.
     * @return the result of the search.
     */
    @GetMapping("/_search/staples")
    public List<Staple> searchStaples(@RequestParam String query) {
        log.debug("REST request to search Staples for query {}", query);
        return StreamSupport
            .stream(stapleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
