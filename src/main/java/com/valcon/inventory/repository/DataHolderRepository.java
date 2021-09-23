package com.valcon.inventory.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.valcon.inventory.entity.DataHolder;

/**
 * @author rjez
 *
 */
@Repository
public interface DataHolderRepository extends CrudRepository<DataHolder, Long> {

	Optional<DataHolder> findOneByDocumentId(Long docId);
}
