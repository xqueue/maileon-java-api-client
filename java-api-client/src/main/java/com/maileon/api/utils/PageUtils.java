package com.maileon.api.utils;

import com.maileon.api.Page;
import com.maileon.api.ResponseWrapper;
import javax.ws.rs.core.MultivaluedMap;

/**
 * This helper class manages {@link Page} creation and management
 *
 */
public class PageUtils {

    /**
     * This method generates a page object with the given parameters.
     *
     * @param pageIndex The index of the page (starting from 1)
     * @param pageSize The size of the page (elements on each page)
     * @param totalItems The total number of elements available
     * @param countPages The number of pages available
     * @return a page
     */
    public static <T> Page<T> createPage(int pageIndex, int pageSize, int totalItems, int countPages) {
        Page<T> page = new Page<>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        page.setTotalItems(totalItems);
        page.setNumberOfPages(countPages);
        return page;
    }

    /**
     * This method generates a page object with the given parameters from a response object.
     *
     * @param pageIndex The index of the page (starting from 1)
     * @param pageSize The size of the page (elements on each page)
     * @param response The response from the webservice that is parsed for X-Items and X-Pages to generate the new page
     * @return a page
     */
    public static <T> Page<T> createPage(int pageIndex, int pageSize, ResponseWrapper response) {
        MultivaluedMap headers = response.getHeaders();
        return createPage(pageIndex, pageSize, Integer.parseInt((String) headers.getFirst("X-Items")), Integer.parseInt((String) headers.getFirst("X-Pages")));
    }

    private PageUtils() {
    }
}
