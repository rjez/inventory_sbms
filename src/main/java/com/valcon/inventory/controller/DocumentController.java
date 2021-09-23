package com.valcon.inventory.controller;

import java.util.Optional;

import com.valcon.inventory.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.valcon.inventory.dto.DocumentDto;
import com.valcon.inventory.entity.DataHolder;
import com.valcon.inventory.entity.Document;
import com.valcon.inventory.repository.DataHolderRepository;
import com.valcon.inventory.repository.DocumentRepository;

@RestController // This means that this class is a Controller
@RequestMapping(path = "/documents") // This means URL's start with /xxx (after Application path)
@CrossOrigin(origins = "*")
public class DocumentController implements ControllerSupport {

	private DocumentRepository docRepository;
	private DataHolderRepository dataRepository;
	private DocumentMapper modelMapper;

	@Autowired
	public DocumentController(DocumentRepository docRepository,
							  DataHolderRepository dataRepository,
							  DocumentMapper modelMapper) {
		this.docRepository = docRepository;
		this.dataRepository = dataRepository;
		this.modelMapper = modelMapper;
	}

	@GetMapping(path = PATH_ID_PARAM)
	public @ResponseBody ResponseEntity<DocumentDto> get(@PathVariable(value = "id") long id) {
		Optional<Document> od = docRepository.findById(id);
		if (od.isPresent()) {
			DocumentDto dto = modelMapper.toDto(od.get());
			// dto.setByteData(getDocumentData(id).getBody());
			return ResponseEntity.of(Optional.of(dto));
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping(path = "/{id}/data")
	public @ResponseBody ResponseEntity<byte[]> getDocumentData(@PathVariable(value = "id") long id) {
		Optional<DataHolder> data = dataRepository.findOneByDocumentId(id);
		return data.map(dataHolder -> ResponseEntity.of(Optional.of(dataHolder.getByteData()))).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
