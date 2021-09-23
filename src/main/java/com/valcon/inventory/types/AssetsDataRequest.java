package com.valcon.inventory.types;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * @author rjez
 *
 */
public class AssetsDataRequest extends PageRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String search;
	private Long userId;
	private DeviceType deviceType;
	private LocalDate purchaseFrom, purchaseTo;
	private boolean ignoreDecommissioned;

	public AssetsDataRequest(int page, int size, Sort sort) {
		super(page, size, sort);
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public LocalDate getPurchaseFrom() {
		return purchaseFrom;
	}

	public void setPurchaseFrom(LocalDate purchaseFrom) {
		this.purchaseFrom = purchaseFrom;
	}

	public LocalDate getPurchaseTo() {
		return purchaseTo;
	}

	public void setPurchaseTo(LocalDate purchaseTo) {
		this.purchaseTo = purchaseTo;
	}

	public boolean isIgnoreDecommissioned() {
		return ignoreDecommissioned;
	}

	public void setIgnoreDecommissioned(boolean ignoreDecommissioned) {
		this.ignoreDecommissioned = ignoreDecommissioned;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public static AssetsDataRequest build(Integer page, Integer size, Optional<String> sort, Optional<String> dir,
			Optional<String> search, Optional<Long> userId, Optional<DeviceType> deviceType,
			Optional<LocalDate> purchaseFrom, Optional<LocalDate> purchaseTo, Optional<Boolean> ignoreDecommissioned) {
		Sort so = null;
		if (sort.isPresent()) {
			Direction direction = null;
			try {
				if (dir.isPresent()) {
					direction = Direction.valueOf(dir.get().toUpperCase());
				}
			} finally {
				if (direction == null) {
					direction = Direction.ASC;
				}
			}
			so = Sort.by(direction, sort.get());
		} else {
			so = Sort.unsorted();
		}
		AssetsDataRequest req = new AssetsDataRequest(page, size, so);
		if (search.isPresent()) {
			req.setSearch(search.get());
		}
		if (userId.isPresent()) {
			req.setUserId(userId.get());
		}
		if (deviceType.isPresent()) {
			req.setDeviceType(deviceType.get());
		}
		if (purchaseFrom.isPresent()) {
			req.setPurchaseFrom(purchaseFrom.get());
		}
		if (purchaseTo.isPresent()) {
			req.setPurchaseTo(purchaseTo.get());
		}
		if (ignoreDecommissioned.isPresent()) {
			req.setIgnoreDecommissioned(ignoreDecommissioned.get());
		}
		return req;
	}

}
