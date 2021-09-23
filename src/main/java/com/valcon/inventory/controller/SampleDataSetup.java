package com.valcon.inventory.controller;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.valcon.inventory.entity.Assets;
import com.valcon.inventory.entity.DataHolder;
import com.valcon.inventory.entity.Document;
import com.valcon.inventory.misc.ZipUtils;
import com.valcon.inventory.repository.AssetsRepository;
import com.valcon.inventory.repository.DataHolderRepository;
import com.valcon.inventory.repository.DocumentRepository;
import com.valcon.inventory.types.DeviceType;

@Service
public class SampleDataSetup {
	
	@Autowired
	private Environment environment;

	@Autowired
	private AssetsRepository assetsRepository;

	@Autowired
	private DocumentRepository docRepository;

	@Autowired
	private DataHolderRepository dataRepository;

	@Transactional
	public void createSampleData() throws Exception {
		byte[] bytes = Files.readAllBytes(new File("C:/tmp/test.pdf").toPath());
		
		Assets a1 = new Assets();
		a1.setComment("Koupeno v roce 2019");
		a1.setDescription("Typ Lenovo T480");
		a1.setDeviceType(DeviceType.NOTEBOOK);
		a1.setInternalNo("Int.c.10001");
		a1.setInventoryNo("Inv.c.50001");
		a1.setPurchaseDate(LocalDate.of(2019, 11, 30));
		a1.setPurchaseDocNo("Fa.c.20190015");
		a1.setPurchasePrice(new BigDecimal("30123.00"));
		a1.setUserId(4L);
		a1.setWarrantyPeriod(12);
		
		Document doc1 = new Document();
		doc1.setAsset(assetsRepository.save(a1));
		doc1.setFileName("test.pdf");
		doc1.setMimeType("application/pdf");
		doc1.setLength(bytes.length);
		doc1 = docRepository.save(doc1);
		DataHolder data1 = new DataHolder();
		data1.setData(bytes);
		data1.setDocument(doc1);
		ZipUtils.zip(data1);
		dataRepository.save(data1);
		
		Assets a2 = new Assets();
		a2.setComment("Koupeno v roce 2020");
		a2.setDescription("Typ HP MFP11df");
		a2.setDeviceType(DeviceType.PRINTER);
		a2.setInternalNo("Int.c.10002");
		a2.setInventoryNo("Inv.c.50002");
		a2.setPurchaseDate(LocalDate.of(2020, 11, 30));
		a2.setPurchaseDocNo("Fa.c.20200027");
		a2.setPurchasePrice(new BigDecimal("8789.00"));
		a2.setUserId(5L);
		a2.setWarrantyPeriod(12);
		
		Document doc2 = new Document();
		doc2.setAsset(assetsRepository.save(a2));
		doc2.setFileName("test.pdf");
		doc2.setMimeType("application/pdf");
		doc2.setLength(bytes.length);
		doc2 = docRepository.save(doc2);
		
		DataHolder data2 = new DataHolder();
		data2.setData(bytes);
		data2.setDocument(doc2);
		ZipUtils.zip(data2);
		dataRepository.save(data2);
	}
	
	public boolean isLocaleEnvironment() {
		for (String p : environment.getActiveProfiles()) {
			if (p.equals("local")) {
				return true;
			}
		}
		return false;
	}
}
