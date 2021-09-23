package com.valcon.inventory.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.valcon.inventory.entity.support.EntityId;

/**
 * @author rjez
 *
 */
public interface PagingSortingJpaSpecRepository<T extends EntityId>
		extends PagingAndSortingRepository<T, Long>, JpaSpecificationExecutor<T> {}
