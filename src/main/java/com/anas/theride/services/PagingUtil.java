package com.anas.theride.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PagingUtil {
	
	public static PageRequest getPageRequestObject(int pageNumber, int pageSize, String sortBy, String sortDirection) {
		Sort sort = null;
        if (sortBy.equalsIgnoreCase( "date" )) {
            if (sortDirection.equalsIgnoreCase( "asc" )) {
                sort = Sort.by( Sort.Direction.ASC, "createdAt" );
            } else if (sortDirection.equalsIgnoreCase( "desc" )) {
                sort = Sort.by( Sort.Direction.DESC, "createdAt" );
            }
        }
        
        return PageRequest.of( pageNumber, pageSize, sort ); 
	}

}
