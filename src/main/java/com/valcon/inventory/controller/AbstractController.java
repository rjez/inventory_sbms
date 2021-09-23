package com.valcon.inventory.controller;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.valcon.inventory.entity.support.EntityId;

import io.micrometer.core.annotation.Timed;

@CrossOrigin(origins = "*")
@Timed
public abstract class AbstractController<T extends EntityId> implements ControllerSupport {

	abstract protected CrudRepository<T, Long> getRepository();

	@GetMapping(path = PATH_ID_PARAM)
	public @ResponseBody ResponseEntity<T> get(@PathVariable(value = "id") long id) {
		return ResponseEntity.of(getRepository().findById(id));
	}

	@DeleteMapping(path = PATH_ID_PARAM)
	public @ResponseBody ResponseEntity<T> delete(@PathVariable(value = "id") long id) {
		if (getRepository().existsById(id)) {
			getRepository().deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping(path = PATH_COUNT)
	public @ResponseBody long getCount() {
		return getRepository().count();
	}

	@PostMapping
	public @ResponseBody T createNew(@RequestBody T newEntity) {
		return getRepository().save(newEntity);
	}

	@PutMapping(path = PATH_ID_PARAM)
	public @ResponseBody ResponseEntity<T> update(@PathVariable(value = "id") long id, @RequestBody T entity) {
		if (entity.getId() == id && getRepository().existsById(id)) {
			return ResponseEntity.of(Optional.of(getRepository().save(entity)));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
