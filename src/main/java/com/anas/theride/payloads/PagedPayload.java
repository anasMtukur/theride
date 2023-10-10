package com.anas.theride.payloads;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PagedPayload<T> {	
	private List<?> items;
	private long totalPages;
    private long totalElements;
}
