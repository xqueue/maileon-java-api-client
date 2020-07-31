package com.maileon.api;

import java.util.List;

/**
 * The Class Page.
 *
 * @param <T> the generic type
 */
public class Page<T> {

    /**
     * The total items.
     */
    private int totalItems;

    /**
     * The page index.
     */
    private int pageIndex;

    /**
     * The page size.
     */
    private int pageSize;

    /**
     * The number of pages.
     */
    private int numberOfPages;

    /**
     * The items.
     */
    private List<T> items;

    /**
     * Instantiates a new page.
     */
    public Page() {

    }

    /**
     * Instantiates a new page.
     *
     * @param totalItems the total items
     * @param pageIndex the page index
     * @param pageSize the page size
     * @param numberOfPages the number of pages
     */
    public Page(int totalItems, int pageIndex, int pageSize, int numberOfPages) {
        this.totalItems = totalItems;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.numberOfPages = numberOfPages;
    }

    /**
     * Gets the total items.
     *
     * @return the total items
     */
    public int getTotalItems() {
        return totalItems;
    }

    /**
     * Sets the total items.
     *
     * @param totalItems the new total items
     */
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    /**
     * Gets the page index.
     *
     * @return the page index
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * Sets the page index.
     *
     * @param pageIndex the new page index
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * Gets the page size.
     *
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the page size.
     *
     * @param pageSize the new page size
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets the number of pages.
     *
     * @return the number of pages
     */
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * Sets the number of pages.
     *
     * @param numberOfPages the number of pages
     */
    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * Sets the items.
     *
     * @param items the new items
     */
    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "page{" + pageIndex + " of " + numberOfPages + ". " + items.size() + " items}";
    }
}
