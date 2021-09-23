package com.valcon.inventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import com.valcon.inventory.entity.Assets;
import com.valcon.inventory.types.AssetsDataRequest;

/**
 * @author rjez
 *
 */
@Repository
public interface AssetsRepository extends PagingSortingJpaSpecRepository<Assets> {

	List<Assets> findByUserId(Long userId);

	default long count(AssetsDataRequest req) {
		return count(new AssetSearchSpec(req));
	}

	default Page<Assets> getByRequest(AssetsDataRequest req) {
		return findAll(new AssetSearchSpec(req), req);
	}

}
