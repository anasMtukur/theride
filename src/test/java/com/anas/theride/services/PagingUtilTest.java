package com.anas.theride.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class PagingUtilTest {

    @Test
    void testGetPageRequestObjectWithAscSort() {
        int pageNumber = 1;
        int pageSize = 10;
        String sortBy = "date";
        String sortDirection = "asc";

        PageRequest pageRequest = PagingUtil.getPageRequestObject(pageNumber, pageSize, sortBy, sortDirection);

        assertNotNull(pageRequest);
        assertEquals(pageNumber, pageRequest.getPageNumber());
        assertEquals(pageSize, pageRequest.getPageSize());

        Sort sort = pageRequest.getSort();
        assertNotNull(sort);
        assertTrue(sort.isSorted());
        assertEquals(Sort.Direction.ASC, sort.getOrderFor("createdAt").getDirection());
    }

    @Test
    void testGetPageRequestObjectWithDescSort() {
        int pageNumber = 2;
        int pageSize = 20;
        String sortBy = "date";
        String sortDirection = "desc";

        PageRequest pageRequest = PagingUtil.getPageRequestObject(pageNumber, pageSize, sortBy, sortDirection);

        assertNotNull(pageRequest);
        assertEquals(pageNumber, pageRequest.getPageNumber());
        assertEquals(pageSize, pageRequest.getPageSize());

        Sort sort = pageRequest.getSort();
        assertNotNull(sort);
        assertTrue(sort.isSorted());
        assertEquals(Sort.Direction.DESC, sort.getOrderFor("createdAt").getDirection());
    }

}
