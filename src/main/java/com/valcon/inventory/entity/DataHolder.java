package com.valcon.inventory.entity;

import java.beans.Transient;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.valcon.inventory.entity.support.EntityId;
import com.valcon.inventory.misc.ZipUtils;
import com.valcon.inventory.repository.CompressionType;

/**
 * @author rj
 * @version 1.0
 * @created 17.03.2010 14:49:33
 */
@Entity
@Table(name = "dataholder")
public class DataHolder extends EntityId {

	// Attributes
	public static final String DATA = "data";

	@Column
	@Lob
	private byte[] data;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CompressionType compressionType = CompressionType.NONE;

	@OneToOne(cascade = { CascadeType.REFRESH, CascadeType.DETACH }, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "document_id")
	private Document document;

	public DataHolder() {
	}
	
	public DataHolder(Document doc, byte[] data) {
		this.document = doc;
		this.document.setLength(data.length);
		setByteDataAndZip(data);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public CompressionType getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(CompressionType compressionType) {
		this.compressionType = compressionType;
	}

	@Transient
	public byte[] getByteData() {
		if (CompressionType.NONE.equals(getCompressionType())) {
			return getData();
		}
		return ZipUtils.unzip(this);
	}
	
	@Transient
	public void setByteDataAndZip(byte[] data) {
		this.data = data;
		ZipUtils.zip(this);
	}
}