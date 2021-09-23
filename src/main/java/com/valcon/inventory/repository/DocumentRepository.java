package com.valcon.inventory.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.valcon.inventory.entity.Document;

/**
 * @author rjez
 *
 */
@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
	
	List<Document> findByAssetId(Long assetId);
}
