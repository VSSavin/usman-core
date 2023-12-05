package com.github.vssavin.usmancore.data.pagination;

import org.springframework.data.domain.Page;

import java.util.Objects;

/**
 * Provides data paging.
 *
 * @param <T> the type of the paged element.
 * @author vssavin on 01.12.2023.
 */
public class Paged<T> {

	private Page<T> page;

	private Paging paging;

	public Paged(Page<T> page, Paging paging) {
		this.page = page;
		this.paging = paging;
	}

	public Paged() {
	}

	@Override
	public String toString() {
		return "Paged{" + "page=" + page + ", paging=" + paging + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Paged<?> paged = (Paged<?>) o;
		return page.equals(paged.page) && paging.equals(paged.paging);
	}

	@Override
	public int hashCode() {
		return Objects.hash(page, paging);
	}

	public Page<T> getPage() {
		return page;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}

}
