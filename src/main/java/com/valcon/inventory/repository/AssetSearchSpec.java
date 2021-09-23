package com.valcon.inventory.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.valcon.inventory.entity.Assets;
import com.valcon.inventory.types.AssetsDataRequest;
import com.valcon.inventory.types.DeviceType;

/**
 * @author rjez
 *
 */
public class AssetSearchSpec implements Specification<Assets> {

	private String search;
	private final Long userId;
	private final DeviceType deviceType;
	private final LocalDate purchaseFrom;
	private final LocalDate purchaseTo;
	private final boolean ignoreDecommissioned;

	public AssetSearchSpec(AssetsDataRequest req) {
		this(req.getSearch(), req.getUserId(), req.getDeviceType(), req.getPurchaseFrom(), req.getPurchaseTo(),
				req.isIgnoreDecommissioned());
	}

	public AssetSearchSpec(String search, Long userId, DeviceType deviceType, LocalDate purchaseFrom,
			LocalDate purchaseTo, boolean ignoreDecommissioned) {
		if (!StringUtils.isEmpty(search)) {
			this.search = search + "%";
		}
		this.userId = userId;
		this.deviceType = deviceType;
		this.purchaseFrom = purchaseFrom;
		this.purchaseTo = purchaseTo;
		this.ignoreDecommissioned = ignoreDecommissioned;
	}

	@Override
	public Predicate toPredicate(Root<Assets> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> pList = new ArrayList<>(2);
		if (!StringUtils.isEmpty(search)) {
			Predicate intNoLike = criteriaBuilder.like(root.get("internalNo"), search);
			Predicate invNoLike = criteriaBuilder.like(root.get("inventoryNo"), search);
			pList.add(criteriaBuilder.or(intNoLike, invNoLike));
		}
		if (userId != null) {
			pList.add(criteriaBuilder.equal(root.get("userId"), userId));
		}
		if (deviceType != null) {
			pList.add(criteriaBuilder.equal(root.get("deviceType"), deviceType));
		}
		if (purchaseFrom != null) {
			pList.add(criteriaBuilder.greaterThanOrEqualTo(root.<LocalDate>get("purchaseDate"), purchaseFrom));
		}
		if (purchaseTo != null) {
			pList.add(criteriaBuilder.lessThanOrEqualTo(root.<LocalDate>get("purchaseDate"), purchaseTo));
		}
		if (ignoreDecommissioned) {
			pList.add(criteriaBuilder.isNull(root.get("decommissDate")));
		}
		return criteriaBuilder.and(pList.toArray(new Predicate[0]));
	}

}
