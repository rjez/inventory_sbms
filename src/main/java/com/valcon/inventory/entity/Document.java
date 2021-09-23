package com.valcon.inventory.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valcon.inventory.entity.support.EntityId;

import liquibase.util.file.FilenameUtils;

/**
 * @author rj
 * @version 1.0
 * @created 17.03.2010 14:49:33
 */

@Entity
@Table(name = "document")
public class Document extends EntityId {

	// Attributes
	public static final String FILE_NAME = "fileName";
	public static final String MIME_TYPE = "mimeType";
	public static final String LENGTH = "length";
	public static final String DOCUMENT_TYPE = "type";

	// Attributes definition
	@Column(nullable = false, length = 150)
	private String fileName;

	// according to specification
	@Column(nullable = false, length = 127)
	private String mimeType;

	private Integer length;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.REFRESH }, optional = false)
	@JoinColumn(name = "asset_id")
	private Assets asset;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		fileName = FilenameUtils.getName(fileName);
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + length;
		result = PRIME * result + ((fileName == null) ? 0 : fileName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Document other = (Document) obj;
		if (!length.equals(other.length))
			return false;
		if (fileName == null) {
			return other.fileName == null;
		} else return fileName.equals(other.fileName);
	}

	@JsonIgnore
	public Assets getAsset() {
		return asset;
	}

	@JsonIgnore
	public void setAsset(Assets asset) {
		this.asset = asset;
	}

}