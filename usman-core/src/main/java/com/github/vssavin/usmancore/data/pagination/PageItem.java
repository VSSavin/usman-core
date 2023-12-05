package com.github.vssavin.usmancore.data.pagination;

/**
 * Provides a simple paging data element.
 *
 * @author vssavin on 01.12.2023.
 */
public class PageItem {

	private PageItemType pageItemType;

	private int index;

	private boolean active;

	public PageItem(PageItemType pageItemType, int index, boolean active) {
		this.pageItemType = pageItemType;
		this.index = index;
		this.active = active;
	}

	public PageItem() {
	}

	public PageItemType getPageItemType() {
		return pageItemType;
	}

	public int getIndex() {
		return index;
	}

	public boolean isActive() {
		return active;
	}

	public void setPageItemType(PageItemType pageItemType) {
		this.pageItemType = pageItemType;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public static PageItemBuilder builder() {
		return new PageItemBuilder();
	}

	public static class PageItemBuilder {

		private final PageItem pageItem = new PageItem();

		public PageItemBuilder active(boolean active) {
			pageItem.setActive(active);
			return this;
		}

		public PageItemBuilder index(int index) {
			pageItem.setIndex(index);
			return this;
		}

		public PageItemBuilder pageItemType(PageItemType pageItemType) {
			pageItem.setPageItemType(pageItemType);
			return this;
		}

		public PageItem build() {
			return pageItem;
		}

	}

}
