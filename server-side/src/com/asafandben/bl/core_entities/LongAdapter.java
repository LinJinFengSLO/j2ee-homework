package com.asafandben.bl.core_entities;

import javax.xml.bind.annotation.adapters.XmlAdapter;

	public class LongAdapter extends XmlAdapter<String, Long> {
		@Override
	    public Long unmarshal(String s) {
	        return Long.parseLong(s);
	    }
		@Override
	    public String marshal(Long number) {
	        if (number == null) return "";
	        
	        return number.toString();
	    }
}
