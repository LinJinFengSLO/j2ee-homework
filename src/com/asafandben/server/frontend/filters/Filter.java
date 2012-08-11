package com.asafandben.server.frontend.filters;

import java.io.IOException;

import javax.servlet.ServletException;

import com.asafandben.server.frontend.filters.FiltersManager.FilterArguments;

public interface Filter {
    /**
     * Executed before chain.doFilter is called.
     * 
     * @param args
     *            the refcopy of args, so they can be replaced if needed
     * @throws IOException
     * @throws ServletException
     */
    public void doInbound(FilterArguments args) throws IOException,
        ServletException;

    /**
     * Executed after chain.doFilter is called.
     * 
     * @param args
     *            the refcopy of args, so they can be replaced if needed
     * @throws IOException
     * @throws ServletException
     */
    public void doOutbound(FilterArguments args) throws IOException,
        ServletException;
    
    /**
     * getPath for current Filter.

	 * @return paths which current filter should be activated on.
     */    
    public String[] getPath();
}
