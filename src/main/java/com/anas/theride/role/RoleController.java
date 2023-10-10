package com.anas.theride.role;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anas.theride.payloads.PagedPayload;
import com.anas.theride.services.PagingUtil;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RequestMapping( value="/v1/roles" )
public class RoleController {
	@Autowired
	RoleRepository repository;
	
	@Autowired
	RoleService service;

	@GetMapping( produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<PagedPayload<Role>> find(
			Principal principal,
			@RequestParam(name = "page", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "search", required = false, defaultValue = StringUtils.EMPTY) String searchCriteria,
            @RequestParam(name = "searchtext", required = false, defaultValue = "") String searchText,
            @RequestParam(name = "pagesize", required = false, defaultValue = "1000") int pageSize,
            @RequestParam(name = "sortby", required = false, defaultValue = "date") String sortBy,
            @RequestParam(name = "sortdirection", required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(name = "createdBy", required = false, defaultValue = "") String createdBy) throws Exception {
		
		PagedPayload<Role> response = new PagedPayload<Role>();
		PageRequest pageRequest = PagingUtil.getPageRequestObject( pageNumber, pageSize, sortBy, sortDirection );
		Page<Role> pageData = repository.findAll(pageRequest);
		
		List<Payload> payloads = pageData.getContent()
				.stream()
				.map((p) -> service.fromModel(p))
				.collect( Collectors.toList() );
        
		response.setTotalPages( pageData.getTotalPages() );
		response.setTotalElements( pageData.getTotalElements() );
		response.setItems(payloads);
		
		return ResponseEntity.ok( response );
	}
	
	@PostMapping
	public ResponseEntity<String> add(
			Principal principal,
			@Valid @RequestBody Payload payload ) throws Exception {
		
		
		Role entity = service.fromPayload(payload);
		repository.save( entity );
		
		return ResponseEntity.ok( String.valueOf( entity.getId() ) );
	}
	
	@PutMapping( value="/{id}" )
	public ResponseEntity<String> update(
			Principal principal,
			@PathVariable( name = "id", required = true ) String id,
			@Valid @RequestBody Payload payload ) throws Exception {
		
		Role entity = repository.findById( UUID.fromString( id ) )
				.orElseThrow();
		
		repository.save( entity );
		
		return ResponseEntity.ok( String.valueOf( entity.getId() ) );
	}
	
	@DeleteMapping( value="/{id}" )
	public ResponseEntity<String> delete(
			Principal principal,
			@PathVariable( name = "id", required = true ) String id ) throws Exception {
		
		Role entity = repository.findById( UUID.fromString( id ) )
				.orElseThrow();
		
		repository.delete( entity );
		
		return ResponseEntity.ok().build();
	}
}
