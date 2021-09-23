package com.valcon.inventory.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.valcon.inventory.entity.DataHolder;
import com.valcon.inventory.repository.CompressionType;

/**
 * @author rjez
 * 
 */
public class ZipUtils {

	private final static Logger LOG = LoggerFactory.getLogger(ZipUtils.class);
	static final int BUFFER = 2048;

	/**
	 * @param data
	 */
	public static void zip(DataHolder data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data.getData());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		zip(in, out, CompressionType.GZIP);
		data.setCompressionType(CompressionType.GZIP);
		data.setData(out.toByteArray());
	}

	/**
	 * @return
	 */
	public static byte[] unzip(DataHolder data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data.getData());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		unzip(in, out, data.getCompressionType());
		return out.toByteArray();
	}

	/**
	 * @param s
	 */
	public static byte[] zip(String s) {
		if (StringUtils.isEmpty(s))
			return new byte[0];

		byte[] buffer = new byte[BUFFER];
		int count;

		try {
			byte[] input = s.getBytes(StandardCharsets.UTF_8);

			ByteArrayInputStream in = new ByteArrayInputStream(input);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DeflaterOutputStream dout = new DeflaterOutputStream(out);

			while ((count = in.read(buffer)) != -1) {
				dout.write(buffer, 0, count);
			}
			in.close();
			out.close();
			dout.close();
			return out.toByteArray();
		} catch (Exception e) {
			LOG.error(null, e);
		}
		return null;
	}

	/**
	 * @return
	 */
	public static String unzip(byte[] data) {
		if (data == null)
			return null;
		else if (data.length == 0)
			return "";

		byte[] buffer = new byte[BUFFER];
		int count;

		try {
			// Decompress the bytes
			InflaterInputStream iin = new InflaterInputStream(
					new ByteArrayInputStream(data));
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			while ((count = iin.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();
			iin.close();
			// Decode the bytes into a String
			return new String(out.toByteArray(), "UTF-8");
		} catch (Exception e) {
			LOG.error(null, e);
		}
		return null;
	}

	/**
	 * @param in
	 * @param out
	 * @param compressionType
	 */
	public static void zip(InputStream in, OutputStream out,
			CompressionType compressionType) {

		try {
			OutputStream zipOut = null;
			if (compressionType == CompressionType.GZIP) {
				zipOut = new GZIPOutputStream(out);
			} else {
				throw new IllegalArgumentException("Illegal compress method");
			}
			byte[] data = new byte[BUFFER];
			int count = 0;
			while ((count = in.read(data, 0, BUFFER)) != -1) {
				zipOut.write(data, 0, count);
			}
			in.close();
			zipOut.close();
		} catch (Exception e) {
			LOG.error("Couldn't create ZIP output stream", e);
		}
	}

	/**
	 * @param in
	 * @param out
	 * @param compressionType
	 */
	public static void unzip(InputStream in, OutputStream out,
			CompressionType compressionType) {
		try {
			InputStream zipIn = null;
			if (compressionType == CompressionType.GZIP) {
				zipIn = new GZIPInputStream(in);
			} else {
				throw new IllegalArgumentException("Illegal compress method");
			}
			byte[] data = new byte[BUFFER];
			int count = 0;
			while ((count = zipIn.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			zipIn.close();
			out.close();
		} catch (Exception e) {
			LOG.error("Couldn't create ZIP output stream", e);
		}
	}
	
}
