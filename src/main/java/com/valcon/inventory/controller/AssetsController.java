package com.valcon.inventory.controller;

import com.valcon.inventory.dto.DocumentDto;
import com.valcon.inventory.dto.UserDTO;
import com.valcon.inventory.entity.Assets;
import com.valcon.inventory.entity.DataHolder;
import com.valcon.inventory.entity.Document;
import com.valcon.inventory.mapper.DocumentMapper;
import com.valcon.inventory.repository.AssetsRepository;
import com.valcon.inventory.repository.DataHolderRepository;
import com.valcon.inventory.repository.DocumentRepository;
import com.valcon.inventory.service.UserDataProvider;
import com.valcon.inventory.types.AssetsDataRequest;
import com.valcon.inventory.types.DeviceType;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Timed
@RestController // This means that this class is a Controller
@RequestMapping(path = "/assets") // This means URL's start with /xxx (after Application path)
public class AssetsController extends AbstractController<Assets> {

	final static Logger LOG = LoggerFactory.getLogger(AssetsController.class);

	private AssetsRepository assetsRepository;
	private DocumentRepository documentRepository;
	private UserDataProvider userDataProvider;
	private DataHolderRepository dataRepository;
	private DocumentMapper modelMapper;

	@Override
	protected CrudRepository<Assets, Long> getRepository() {
		return assetsRepository;
	}

	@GetMapping(path = "/{id}/documents")
	public @ResponseBody List<DocumentDto> getDocuments(@PathVariable Long id) {
		return documentRepository.findByAssetId(id).stream()
				.map(modelMapper::toDto).collect(Collectors.toList());
	}

	@PreAuthorize("hasAnyAuthority('ACCOUNTANT', 'ADMIN')")
	@DeleteMapping(path = "/{id}/documents/{documentId}")
	public @ResponseBody ResponseEntity<DocumentDto> deleteDocument(@PathVariable(value = "id") long assetId,
			@PathVariable(value = "documentId") long documentId) {
		if (assetsRepository.existsById(assetId) && documentRepository.existsById(documentId)) {
			Optional<DataHolder> od = dataRepository.findOneByDocumentId(documentId);
			od.ifPresent(dataHolder -> dataRepository.delete(dataHolder));
			documentRepository.deleteById(documentId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PreAuthorize("hasAnyAuthority('ACCOUNTANT', 'ADMIN')")
	@PostMapping(path = "/{id}/documents")
	public @ResponseBody ResponseEntity<DocumentDto> saveDocument(@RequestParam("file") MultipartFile file,
			@PathVariable Long id) {
		Optional<Assets> asset = assetsRepository.findById(id);
		if (asset.isPresent()) {
			if (file == null || file.getOriginalFilename() == null) {
				LOG.error("File or file name in request is empty.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			try {
				Document doc = new Document();
				doc.setAsset(asset.get());
				doc.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
				doc.setMimeType(URLConnection.guessContentTypeFromName(doc.getFileName()));
				doc.setLength(file.getBytes().length);
				doc = documentRepository.save(doc);

				dataRepository.save(new DataHolder(doc, file.getBytes()));

				return ResponseEntity.ok(modelMapper.toDto(doc));
			} catch (Exception e) {
				LOG.error(null, e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping
	@SuppressWarnings("unchecked")
	public @ResponseBody List<Assets> getAssets(//
			@RequestParam(name = "page", defaultValue = "0") Integer page, //
			@RequestParam(name = "size", defaultValue = "10") Integer size, //
			@RequestParam("sort") Optional<String> sort, //
			@RequestParam("dir") Optional<String> dir, //
			@RequestParam("search") Optional<String> search, //
			@RequestParam("userId") Optional<Long> userId, //
			@RequestParam("deviceType") Optional<DeviceType> deviceType, //
			@RequestParam("purchaseFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> purchaseFrom, //
			@RequestParam("purchaseTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> purchaseTo, //
			@RequestParam("ignoreDecommissioned") Optional<Boolean> ignoreDecommissioned, //
			@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeaderValue) {
		LOG.info("Get assets.");
		List<Assets> list = null;
		if (page == 0 && size == 0) {
			list = Collections.EMPTY_LIST;
		} else {
			list = assetsRepository.getByRequest(AssetsDataRequest.build(page, size, sort, dir, search, userId,
					deviceType, purchaseFrom, purchaseTo, ignoreDecommissioned)).getContent();
			// get unique user ids
			Set<Long> userIdSet = new HashSet<>(list.size());
			list.stream().filter(p -> (p.getUserId() != null && userIdSet.add(p.getUserId()))).collect(Collectors.toList());
			// get user map
			Map<Long, UserDTO> userMap = userDataProvider.loadUsers(List.copyOf(userIdSet),
					authorizationHeaderValue);
			// set user data to assets
			list.forEach(a -> {
				if (userMap.containsKey(a.getUserId())) {
					UserDTO u = userMap.get(a.getUserId());
					a.setFirstName(u.getFirstName());
					a.setLastName(u.getLastName());
				}
			});
		}
		return list;
	}

	@GetMapping(path = "countBy")
	public @ResponseBody long getCount(@RequestParam("search") Optional<String> search, //
			@RequestParam("userId") Optional<Long> userId, //
			@RequestParam("deviceType") Optional<DeviceType> deviceType, //
			@RequestParam("purchaseFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> purchaseFrom, //
			@RequestParam("purchaseTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> purchaseTo, //
			@RequestParam("ignoreDecommissioned") Optional<Boolean> ignoreDecommissioned) {
		if (search.isPresent() //
				|| userId.isPresent() || deviceType.isPresent() || purchaseFrom.isPresent() || purchaseTo.isPresent()
				|| ignoreDecommissioned.isPresent()) {
			return assetsRepository.count(AssetsDataRequest.build(0, 1, Optional.empty(), Optional.empty(), search,
					userId, deviceType, purchaseFrom, purchaseTo, ignoreDecommissioned));
		} else {
			return assetsRepository.count();
		}
	}

	@Autowired
	public void setAssetsRepository(AssetsRepository assetsRepository) {
		this.assetsRepository = assetsRepository;
	}

	@Autowired
	public void setDocumentRepository(DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}

	@Autowired
	public void setUserDataProvider(UserDataProvider userDataProvider) {
		this.userDataProvider = userDataProvider;
	}

	@Autowired
	public void setDataRepository(DataHolderRepository dataRepository) {
		this.dataRepository = dataRepository;
	}

	@Autowired
	public void setModelMapper(DocumentMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
}