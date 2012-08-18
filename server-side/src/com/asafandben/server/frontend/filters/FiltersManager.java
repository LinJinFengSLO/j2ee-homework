package com.asafandben.server.frontend.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.asafandben.utilities.StringUtilities;

@WebFilter(filterName = "FilterManager", description = "Base Filter which runs all filters.", urlPatterns = {"*"})
public class FiltersManager implements javax.servlet.Filter {

    public enum FilterState {
        PROCEED, FAIL, FAIL_FILTERS_RUN_REQUEST
    }

    public static class FilterArguments {
        public final HttpServletRequest request;
        public HttpServletResponse response;
        public final Map<String, Object> stash;
        public FilterState state;

        public FilterArguments(HttpServletRequest req, HttpServletResponse res) {
            this.request = req;
            this.response = res;
            this.stash = new HashMap<String, Object>();
            this.state = FilterState.PROCEED;
        }

        public FilterArguments(HttpServletRequest req, HttpServletResponse res,
                        Map<String, Object> stash, FilterState state) {
            this.request = req;
            this.response = res;
            this.stash = stash;
            this.state = state;
        }
    }
          
    private Map<String, Set<Filter>> filtersByRegex = new HashMap<String, Set<Filter>>();
    
    private void add (Filter filterEntry) {
    	for (String currPath : filterEntry.getPath()) {
    		Set<Filter> filters = filtersByRegex.get(currPath);
    		
    		if (filters == null) {
    			filters = new HashSet<Filter>();
    		}
    		
    		filters.add(filterEntry);
    		filtersByRegex.put(currPath, filters);
    	}
    }
    
    private Set<Filter> findFiltersByRegExpression(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        Set<Filter> filtersToRun = new HashSet<Filter>();
        
        for (Entry<String, Set<Filter>> e : filtersByRegex.entrySet()) {
            if (request.getRequestURI().startsWith(e.getKey())) {
            	filtersToRun.addAll(e.getValue());
            }
        }
        return filtersToRun;
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Filters Manager started.");
		add(new IsLoggedInFilter());
		
		
	}

	@Override
	public void doFilter(ServletRequest request, 
			ServletResponse response,
			FilterChain chain)		throws IOException, ServletException {

		
		// Make sure all of our request/responses are handled as UTF-8.
		request.setCharacterEncoding(StringUtilities.UTF_8);
		
		// Start our FilterArguments for this session.
	    Map<String, Object> stash = new HashMap<String, Object>();
	    FilterArguments args = new FilterArguments((HttpServletRequest)request, (HttpServletResponse)response, stash, FilterState.PROCEED);
		
	    // Find which filters should run using our findFiltersByRegExpression method and the URI string.
	    Set<Filter> filtersToRun = findFiltersByRegExpression(request);
	    
	    boolean runRequest = true;
	    try {
	    	for (Filter currentFilter : filtersToRun) {
	    		currentFilter.doInbound(args);
	    		
	    		if (args.state == FilterState.FAIL) {
		    		runRequest = false;
		    		break;
		    	}
	    		if (args.state == FilterState.FAIL_FILTERS_RUN_REQUEST) {
	    			break;
	    		}
	    	}
	    	
	    	if (runRequest) {
	    		chain.doFilter(args.request, args.response);
	    	}	    	
	    }
	    catch (Throwable e) {
	    	((HttpServletResponse)response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
	    }
	    finally {
	    	for (Filter currentFilter : filtersToRun) {
	    		currentFilter.doOutbound(args);
	    	}
	    }
	    
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	

       
    
    
    
    
}
