package com.valcon.inventory.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valcon.inventory.entity.support.EntityId;

/**
 * @author rj
 * @version 1.0
 * @created 17.03.2010 14:49:33
 */

public class DocumentDto extends EntityId {

	private String fileName;

	private String mimeType;

	private Integer length;

	private byte[] byteData;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
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

	@JsonIgnore
	public void setLength(Integer length) {
		this.length = length;
	}

	@JsonIgnore
	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] data) {
		this.byteData = data;
	}

	@Override
	public String toString() {
		return "File: " + fileName + " of decl.size " +
				length +
				" (real size: " +
				(byteData == null ? 0 : byteData.length) +
				")" +
				" and type " +
				mimeType;
	}
}