package com.valcon.inventory.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valcon.inventory.entity.support.EntityId;
import com.valcon.inventory.types.DeviceType;

/**
 * @author rjez
 *
 */
@Entity
public class Assets extends EntityId {

	@Column(length = 20, nullable = false)
	private String internalNo;
	@Column(length = 20, nullable = false)
	private String inventoryNo;

	@Column(nullable = false)
	private LocalDate purchaseDate;
	@Column
	private LocalDate decommissDate;
	@Column(nullable = false)
	private String description;
	@Column(length = 50)
	private String purchaseDocNo;
	@Column(nullable = false)
	private BigDecimal purchasePrice;
	@Column(length = 2)
	private Integer warrantyPeriod;
	@Column(length = 5)
	private Long userId;
	@Column
	private String comment;
	@Column
	@Enumerated(EnumType.STRING)
	private DeviceType deviceType;

	@Transient
	private String firstName;
	@Transient
	private String lastName;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "asset")
	private List<Document> documents;

	public String getInternalNo() {
		return internalNo;
	}

	public void setInternalNo(String internalNo) {
		this.internalNo = internalNo;
	}

	public String getInventoryNo() {
		return inventoryNo;
	}

	public void setInventoryNo(String inventoryNo) {
		this.inventoryNo = inventoryNo;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public LocalDate getDecommissDate() {
		return decommissDate;
	}

	public void setDecommissDate(LocalDate decommissDate) {
		this.decommissDate = decommissDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPurchaseDocNo() {
		return purchaseDocNo;
	}

	public void setPurchaseDocNo(String purchaseDocNo) {
		this.purchaseDocNo = purchaseDocNo;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Integer getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(Integer warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	@JsonIgnore
	public List<Document> getDocuments() {
		if (documents == null) {
			documents = new ArrayList<>();
		}
		return documents;
	}

	@JsonIgnore
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	@Transient
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Transient
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
