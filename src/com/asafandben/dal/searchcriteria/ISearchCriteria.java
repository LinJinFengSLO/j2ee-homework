package com.asafandben.dal.searchcriteria;

import java.util.Comparator;

public interface ISearchCriteria<T> extends Comparator<T> {
	public String getQueryString();
}
