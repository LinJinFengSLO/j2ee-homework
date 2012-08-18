package com.asafandben.dal.searchcriteria;

import java.util.Comparator;

public interface ISearchCriteria<T> extends Comparator<T> {
	
	// Example of QueryString: email='chucky@gmail.com'
	public String getQueryString();
	
	// Example of value for email='chucky@gmail.com' is chucky@gmail.com
	public String getSearchValue();

	// Example of Search Operator for email='chucky@gmail.com' is =
	public String getSearchOperator(); 

	// Example of field for email='chucky@gmail.com' is email
	public String getSearchField(); 
	
}
